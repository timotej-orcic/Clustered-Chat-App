import { Component, OnInit, NgModule } from '@angular/core';
import { HelperFunctions } from '../../shared/util/helper-functions';
import { SocketService } from '../../shared/util/services/socket.service';
import { Message } from '../../shared/model/message';
import {Router} from '@angular/router';

import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers: [SocketService]
})

export class LoginComponent  implements OnInit {
  private errorMessage = null;
  private logInfo = {
    uname: '',
    password: ''
  };
  private ioConnection: any;
  private messages: Message[] = [];

  private disabled: boolean;

  constructor(private socketService: SocketService) {
  }

  ngOnInit() {
    this.initIoConnection();
  }

  private initIoConnection(): void {
    this.socketService.initSocket();

    this.ioConnection = this.socketService.onMessage()
      .subscribe((message: Message) => {
        this.messages.push(message);
      });
  }

  public btnClick(): void {
    const msg = new Message('foo', 'bar');
    this.sendMessage(msg);
  }

  public sendMessage(message: Message): void {
    if (!message) {
      return;
    }

    this.socketService.send(message);
  }

  clearAllInfo() {
    this.logInfo.uname = '';
    this.logInfo.password = '';
  }

  tryLogin() {
    if (!HelperFunctions.containsEmptyValues(this.logInfo)) {
      this.errorMessage = null;
      // this.sendMessage(JSON.stringify(this.logInfo));
    }
  }

  hideError() {
    this.errorMessage = null;
  }
}
