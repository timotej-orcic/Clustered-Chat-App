import { Component, OnInit, NgModule } from '@angular/core';
import { HelperFunctions } from '../../shared/util/helper-functions';
import { SocketService } from '../../shared/util/services/socket.service';
import { Message } from '../../shared/model/message';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

// Socket.io events
export enum Event {
  CONNECT = 'connect',
  DISCONNECT = 'disconnect'
}

export class LoginComponent  implements OnInit {
  private errorMessage = null;
  private logInfo = {
    uname: '',
    password: ''
  };
  private router = null;
  private http = null;
  private ioConnection: any;
  private messages: Message[] = [];

  constructor(protected rt: Router, private socketService: SocketService) {
    this.router = rt;
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

    this.socketService.onEvent(Event.CONNECT)
      .subscribe(() => {
        console.log('connected');
      });
      
    this.socketService.onEvent(Event.DISCONNECT)
      .subscribe(() => {
        console.log('disconnected');
      });
  }

  clearAllInfo() {
    this.logInfo.uname = '';
    this.logInfo.password = '';
  }

  tryLogin() {
    if (!HelperFunctions.containsEmptyValues(this.logInfo)) {
      this.errorMessage = null;

      // WS konekcija
    }
  }

  hideError() {
    this.errorMessage = null;
  }
}
