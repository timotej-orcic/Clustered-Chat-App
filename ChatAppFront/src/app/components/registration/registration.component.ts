import { Component, OnInit } from '@angular/core';
import { HelperFunctions } from '../../shared/util/helper-functions';
import {Router} from '@angular/router';
import { SocketService } from '../../shared/util/services/socket.service';
import { Message } from '../../shared/model/message';
import { UserRegistrationInfo } from '../../model/user-registration-info';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {
  private registered = false;
  private repeatPW = '';
  private regInfo = {
    username: null,
    password: null,
    name: null,
    lastname: null
  };
  private errorMessage = null;
  private router = null;
  private http = null;
  private msg: Message = new Message("register", null, null);

  constructor(protected rt: Router, private socketService: SocketService) {
    this.router = rt;
  }

  ngOnInit() {
  }

  tryRegister() {
    const areAnyEmptyValues = HelperFunctions.containsEmptyValues(this.regInfo);
    const arePasswordsMatching = this.regInfo.password === this.repeatPW;
    const shouldSendToServer = !areAnyEmptyValues && arePasswordsMatching;

    if (shouldSendToServer) {
      const user = new UserRegistrationInfo(this.regInfo.username, this.regInfo.password,
                                            this.regInfo.name, this.regInfo.lastname);
      this.msg.content = JSON.stringify(this.regInfo);
      this.msg.loggedUserName = null;
      console.log(this.msg);
      this.socketService.send(this.msg);
    } else {
      this.clearImportantDetails();
      if (arePasswordsMatching === false) {
        this.errorMessage = 'Passwords don\'t match. Please, try again.';
      } else if (areAnyEmptyValues) {
        this.errorMessage = 'Some fields were left empty. Please, fill in the form and try again.';
      } else {
        this.errorMessage = 'Error registering you. Please, try again.';
      }
    }
  }

  hideError() {
    this.errorMessage = null;
  }

  validateInformation(param): boolean {
    return HelperFunctions.isEmptyValue(param);
  }

  clearAllInfo() {
    this.regInfo.password = '';
    this.repeatPW = '';
    this.regInfo.name = '';
    this.regInfo.lastname = '';
    this.regInfo.username = '';
  }

  clearImportantDetails() {
    this.regInfo.password = '';
    this.repeatPW = '';
  }
}
