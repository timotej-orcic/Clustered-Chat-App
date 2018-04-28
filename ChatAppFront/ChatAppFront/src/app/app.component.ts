import { Component, OnInit, NgModule } from '@angular/core';
import { SocketService } from './shared/util/services/socket.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  public static readonly socketService: SocketService

  constructor(public socketService: SocketService) {
  }

  ngOnInit() {
    this.socketService.initSocket();
  }

}
