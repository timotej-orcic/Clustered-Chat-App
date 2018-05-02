import { Component, OnInit, NgModule, EventEmitter, Input, Output } from '@angular/core';
import { HelperFunctions } from '../../shared/util/helper-functions';
import { SocketService } from '../../shared/util/services/socket.service';
import { Message } from '../../shared/model/message';
import {Router} from '@angular/router';
import { HeaderComponent } from '../header/header.component';
import { HomeComponent } from '../home/home.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent  implements OnInit {
  private errorMessage = null;
  private logInfo = {
    uname: '',
    password: ''
  };

  constructor(private socketService: SocketService, private router : Router) {
  }

  ngOnInit() {
  }

  /*public btnClick(): void {
    const msg = new Message('foo', 'bar');
    this.sendMessage(msg);
  }*/

  public sendMessage(message: Message): void {
    if (!message) {
      return;
    }
    this.socketService.send(message);

    this.socketService.socket.onmessage = function(event){
      localStorage.setItem('logovanKorisnik',event.data);
      HeaderComponent.updateUserStatus.next(true);
      HomeComponent.updateUserStatus.next(true);
    }

    this.router.navigate(['']);

  }

  clearAllInfo() {
    this.logInfo.uname = '';
    this.logInfo.password = '';
  }

  tryLogin() {
    if (!HelperFunctions.containsEmptyValues(this.logInfo)) {
      this.errorMessage = null;
      const msg = new Message('login', JSON.stringify(this.logInfo), null);
      this.sendMessage(msg);
    }
  }

  hideError() {
    this.errorMessage = null;
  }
}
