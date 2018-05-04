import {Component, Input, OnInit} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Subject } from 'rxjs/Subject';
import { SocketService } from '../../shared/util/services/socket.service';
import { Message } from '../../shared/model/message';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  @Input()
  private authenticated: boolean = false;
  private logovan : string;

  constructor(private socketService : SocketService) {
  }

  ngOnInit() {
    HeaderComponent.updateUserStatus.subscribe(res => {
      this.logovan = localStorage.getItem('logovanKorisnik');
      if(this.logovan!=null){
        this.authenticated = true;
      }
    })
    
    this.logovan = localStorage.getItem('logovanKorisnik');
      if(this.logovan!=null){
        this.authenticated = true;
      }
    
  }
  
  logout() {
    localStorage.removeItem("logovanKorisnik");
    this.authenticated = false;

    const msg = new Message('logout', null, localStorage.getItem('logovanKorisnik'));
    this.sendMessage(msg);

    window.location.reload();
  }

  public sendMessage(message: Message): void {
    if (!message) {
      return;
    }
    this.socketService.send(message);
  }

  public static updateUserStatus: Subject<boolean> = new Subject();

}
