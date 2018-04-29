import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Observer } from 'rxjs/Observer';
import { Message } from '../../model/message';
import { Event } from '../../model/event';

const SERVER_URL = 'ws://localhost:8081/ChatApp_DWA/websocket';
//gasgasgasg
@Injectable()
export class SocketService {

  public socket : WebSocket;
  constructor() { }

  public initSocket() : void{
      this.socket = new WebSocket(SERVER_URL);
  }

  public send(message: Message): void {
    this.socket.send(JSON.stringify(message));
  }

      
  public onMessage(): Observable<Message> {
    return new Observable<Message>(observer => {
      this.socket.onmessage = (event) => { console.log('Djes mala'); };
    });
}

  /*public onEvent(event: Event): Observable<any> {
      return new Observable<Event>(observer => {
          this.socket.onEvent(event, () => observer.next());
      });
  }*/

  public closeSocket(): void {
      this.socket.close();
  }
}

// Socket.io events
/*export enum Event {
    CONNECT = 'connect',
    DISCONNECT = 'disconnect'
}*/
