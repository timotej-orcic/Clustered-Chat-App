import { Component, OnInit } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { SocketService } from '../../shared/util/services/socket.service';
import { Message } from '../../shared/model/message';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  private logovan : string;
  private authenticated : boolean = false;
  private participants : any;
  private messages : any;
  private active : string = null;
  private messageInfo = {
    active : null,
    sendText : ""
  };

  constructor(private socketService : SocketService) { }

  ngOnInit() {
    HomeComponent.updateUserStatus.subscribe(res => {
      this.logovan = localStorage.getItem('logovanKorisnik');
      if(this.logovan!=null){
        this.authenticated = true;
      }
        if(this.authenticated){
          if(this.socketService.socket.readyState===1){
            this.getParticipants();
          }else{
            this.socketService.socket.onopen = (event) => { 
              this.getParticipants();
            }
          }
        }

    })
    this.logovan = localStorage.getItem('logovanKorisnik');
      if(this.logovan!=null){
        this.authenticated = true;
      }

      if(this.authenticated){
        if(this.socketService.socket.readyState===1){
          this.getParticipants();
        }else{
          this.socketService.socket.onopen = (event) =>{
            this.getParticipants();
          }
        }
        
      }

  }

  getParticipants(){
    const msg = new Message('getParticipants', null, localStorage.getItem('logovanKorisnik'));
    this.sendMessage(msg);
  }

  getMessages(otherUser){
    this.messageInfo.active = otherUser;
    this.messageInfo.sendText = "";
    const msg = new Message('getMessages', otherUser, localStorage.getItem('logovanKorisnik'));
    this.sendMessage(msg);
  }

  public sendMessage(message: Message): void {
    if (!message) {
      return;
    }
    this.socketService.send(message);

    this.socketService.socket.onmessage = (event) => { 
      var resp = JSON.parse(event.data);
      if(resp.messageType==="getParticipants"){
        this.participants = JSON.parse(resp.content);
      }else if(resp.messageType==="getMessages"){
        this.messages = JSON.parse(resp.content);
      }else if(resp.messageType==="chat"){
        this.messages.push(JSON.parse(resp.content));
      }
    }
  }

  convertToDate = function(date){
    var d = new Date(parseInt(date, 10));
    return d;
  }

  send(){
    const msg = new Message('chat', JSON.stringify(this.messageInfo), localStorage.getItem('logovanKorisnik'));
    this.sendMessage(msg);
  }
  public static updateUserStatus: Subject<boolean> = new Subject();

}
