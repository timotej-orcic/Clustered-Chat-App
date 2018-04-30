import { Component, OnInit } from '@angular/core';
import { SocketService } from './shared/util/services/socket.service';
import { Message } from './shared/model/message';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  private title = 'app';
  private ioConnection: any;
  private messages: Message[] = [];

  constructor(private socketService: SocketService) {
  }

  ngOnInit() {
    this.initIoConnection();
  }

  private initIoConnection(): void {
    this.socketService.initSocket();
  }

}
