package com.example.SEPDrive.model;


import com.example.SEPDrive.repository.userDAO;
import com.example.SEPDrive.service.rideRequestService;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import io.jenetics.jpx.Length;
import io.jenetics.jpx.WayPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;


@Entity
@Table(name = "ride_requests")
public class rideRequest {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private user customer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Driver_id")
    private Fahrer Driver;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_class", nullable = false, length = 10)
    private carClass carClass;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private RequestStatus status = RequestStatus.Active;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "start_address_id")
    private adress startAddress;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name               = "ride_request_stops",
            joinColumns        = @JoinColumn(name = "ride_request_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private List<adress> zwischenstops = new ArrayList<>();



    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "dest_address_id")
    private adress destAddress;




    @Min(0)
    @Max(5)
    @Column(name = "customer_rating")
    private Double customerRating;


    @Min(0)
    @Max(5)
    @Column(name = "driver_rating")
    private Double drivererRating;



    private double distance;

    private double cost;

    private double duration;









    // Constructors
    public rideRequest() {}

    public rideRequest(user customer,Fahrer Driver, carClass carClass, adress startAddress,
                       adress destAddress,Double drivererRating,Double customerRating) {
        this.customer = customer;
        this.Driver = Driver;
        this.carClass = carClass;
        this.startAddress = startAddress;
        this.destAddress = destAddress;
        this.drivererRating = drivererRating;
        this.customerRating = customerRating;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = RequestStatus.Active;
    }

    public rideRequest(user customer, carClass carClass, adress startAddress, adress destAddress) {
        this.customer = customer;
        this.carClass = carClass;
        this.startAddress = startAddress;
        this.destAddress = destAddress;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = RequestStatus.Active;
        this.customerRating = 0.0;
        this.drivererRating = 0.0;
    }

    public double calculateDistance(adress startAddress, adress destAddress) {
        double startlat = startAddress.getLat().doubleValue();
        double startlong = startAddress.getLng().doubleValue();
        double destlat = destAddress.getLat().doubleValue();
        double destlong = destAddress.getLng().doubleValue();

        WayPoint start = WayPoint.of(startlat, startlong );
        WayPoint dest = WayPoint.of(destlat, destlong );

        this.distance = start.distance(dest).to(Length.Unit.KILOMETER);

        return this.distance;

    }






    public List<adress> getZwischenstops() {
        return zwischenstops;
    }

    public void setZwischenstops(List<adress> zwischenstops) {
        this.zwischenstops = zwischenstops;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public carClass getCarClass() {
        return carClass;
    }

    public void setCarClass(carClass carClass) {
        this.carClass = carClass;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public user getCustomer() {
        return customer;
    }

    public void setCustomer(user customer) {
        this.customer = customer;
    }



    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public adress getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(adress startAddress) {
        this.startAddress = startAddress;
    }

    public adress getDestAddress() {
        return destAddress;
    }

    public void setDestAddress(adress destAddress) {
        this.destAddress = destAddress;
    }

    public Fahrer getDriver() {
        return Driver;
    }

    public void setDriver(Fahrer driver) {
        this.Driver = driver;
    }

    public Double getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(Double customerRating) {
        this.customerRating = customerRating;
    }

    public Double getDrivererRating() {
        return drivererRating;
    }

    public void setDrivererRating(Double drivererRating) {
        this.drivererRating = drivererRating;
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }






}
