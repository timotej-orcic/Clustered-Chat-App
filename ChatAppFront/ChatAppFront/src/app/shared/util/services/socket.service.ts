declare var require: any;

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Observer } from 'rxjs/Observer';

import { Message } from '../../model/message';
import { Event } from '../../model/event';

import * as socketIo from 'socket.io-client';

const SERVER_URL = 'http://localhost:8090';

@Injectable()
export class SocketService {

  constructor() { }
  private socket;

  public initSocket(): void {
    const express = require('express');
    const http = require('http');
    const app = express();
    const server = app.createServer(app);

    this.socket = socketIo.listen(SERVER_URL);
  }

  public send(message: Message): void {
      this.socket.emit('message', message);
  }

  public onMessage(): Observable<Message> {
      return new Observable<Message>(observer => {
          this.socket.on('message', (data: Message) => observer.next(data));
      });
  }

  public onEvent(event: Event): Observable<any> {
      return new Observable<Event>(observer => {
          this.socket.on(event, () => observer.next());
      });
  }

}
