import { Component } from '@angular/core';
import {ButtonDirective} from "primeng/button";
import {FormsModule} from "@angular/forms";
import {AuthenticationService} from "../../services/authentication/authentication.service";
import {Router} from "@angular/router";
import {TwoFaRequest} from '../../models/two-fa-request';
import {Message} from 'primeng/message';
import {NgIf} from '@angular/common';
import {AutoFocusNextDirective} from '../../services/auto-focus-next.directive';
import { ToastrService } from 'ngx-toastr';
import {WebsocketService} from '../../services/websocket.service';




@Component({
  selector: 'app-two-fa',
  standalone: true,
  imports: [
    ButtonDirective,
    FormsModule,
    Message,
    NgIf,
    AutoFocusNextDirective


  ],
  templateUrl: './two-fa.component.html',
  styleUrl: './two-fa.component.scss'
})
export class TwoFAComponent {

  a: string = '';
  b: string = '';
  c: string = '';

  d: string = '';
  e: string = '';
  f: string = '';

 facode: string = '';

 errorMsg = '';

 twofaRequest: TwoFaRequest = {
   code : '',
   userName: '',
   password: ''
 };




  constructor(
      private authenticationService: AuthenticationService,
      private router: Router,
      private toastr: ToastrService,
      private WebSocketService : WebsocketService,
  ) {}

  updateFacode(): void {

    this.facode = `${this.a}${this.b}${this.c}${this.d}${this.e}${this.f}`;
  }


  verify() {
      this.errorMsg = '';
      const local = localStorage.getItem('login');
      if (local) {
        const json= JSON.parse(local);
        this.twofaRequest.code = this.facode;
        this.twofaRequest.userName = json.userName;
        this.twofaRequest.password = json.password;

      }
      this.authenticationService.TwoFactorLogin(this.twofaRequest)
          .subscribe({
              next: (authenticationResponse) => {
                if (authenticationResponse.kundeDTO) {
                    authenticationResponse.kundeDTO.profilePhoto = undefined;
                }
                  localStorage.setItem('user', JSON.stringify(authenticationResponse));
                  localStorage.removeItem('login');
                  this.router.navigate(['home']);
                  this.toastr.success('login is Successful!', 'Success!!');
                  this.WebSocketService.connect;
              },
              error: (err) => {
                  if (err.error.statusCode === 401 || err.error.statusCode === 403 || err.error.statusCode === 500) {
                      this.errorMsg = 'Invalid code';
                      this.toastr.error('Invalid code Please try again', 'Oups!!');
                  }
              }
          });
  }
}
