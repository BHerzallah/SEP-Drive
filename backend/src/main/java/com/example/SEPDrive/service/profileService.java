package com.example.SEPDrive.service;


import com.example.SEPDrive.model.Fahrer;
import com.example.SEPDrive.model.Kunde;
import com.example.SEPDrive.model.user;
import com.example.SEPDrive.repository.userDAO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.SEPDrive.controller.profileResponseDto;
import com.example.SEPDrive.controller.updateProfileDto;
import com.example.SEPDrive.exceptions.duplicatResourceException;

import java.util.Arrays;
import java.util.List;

@Service
public class profileService {

    @Autowired
    private userDAO userDao;

    @Autowired
    private HttpInterpreter httpInterpreter;



    public profileResponseDto getOwnProfile() {

        user currentUser = userDao.findUserById(httpInterpreter.Interpreter().getId());

        byte[] raw = currentUser.getProfilePhoto();
        byte[] photoBytes = raw == null ? null : Arrays.copyOf(raw, raw.length);

        if(currentUser instanceof Fahrer) {

            return new profileResponseDto(
                    currentUser.getUserName(),
                    currentUser.getFirstName(),
                    currentUser.getLastName(),
                    currentUser.getEmail(),
                    currentUser.getDateOfBirth(),
                    currentUser.getRating(),
                    "Fahrer",
                     currentUser.getTotalRides(),
                    ((Fahrer) currentUser).getCarClass()

            );
        }

        return new profileResponseDto(
                currentUser.getUserName(),
                currentUser.getFirstName(),
                currentUser.getLastName(),
                currentUser.getEmail(),
                currentUser.getDateOfBirth(),
                currentUser.getRating(),
                "Kunde",
                currentUser.getTotalRides()
        );
    }

    public profileResponseDto updateProfile(updateProfileDto dto) {
        String username = getCurrentUsername();
        user currentUser = userDao.findByUserName(username);

        if (!currentUser.getEmail().equals(dto.getEmail())
                && userDao.existsUserByEmail(dto.getEmail())) {
            throw new RuntimeException ("This email is already used by another user.");
        }

        currentUser.setFirstName(dto.getFirstName());
        currentUser.setLastName(dto.getLastName());
        currentUser.setEmail(dto.getEmail());
        currentUser.setProfilePhoto(dto.getProfilePhoto());

        userDao.save(currentUser);

        return new profileResponseDto(
                currentUser.getUserName(),
                currentUser.getFirstName(),
                currentUser.getLastName(),
                currentUser.getEmail(),
                currentUser.getDateOfBirth(),
                currentUser.getRating(),
                "Kunde",
                currentUser.getTotalRides(),
                currentUser.getProfilePhoto()
        );
    }

    public void updatePassword(String newPassword) {
        String username = getCurrentUsername();
        user currentUser = userDao.findByUserName(username);
        currentUser.setPassword(newPassword); // to be encrypted later
        userDao.save(currentUser);
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    public void updateUserName(String newUserName) {
        String currentUserName = getCurrentUsername();
        user currentUser = userDao.findByUserName(currentUserName);

        if (userDao.existsUserByUserName(newUserName)) {
            throw new duplicatResourceException("Dieser Benutzername ist bereits vergeben.");
        }

        currentUser.setUserName(newUserName);
        userDao.save(currentUser);
    }

//    @Transactional
    public byte[] fetchProfilePhoto() {
        user currentUser = userDao.findUserById(httpInterpreter.Interpreter().getId());

        //System.out.println("Profile Photo Length: " + (currentUser.getProfilePhoto() == null ? 0 : currentUser.getProfilePhoto().length));

        return currentUser.getProfilePhoto();

    }

    public String getfilename() {
        user currentUser = userDao.findUserById(httpInterpreter.Interpreter().getId());
        return currentUser.getImageName();
    }


    public Double getrating(String username){
        user user = userDao.findByUserName(username);
        return user.getRating();
    }





}
