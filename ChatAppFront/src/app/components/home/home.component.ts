import { Component, OnInit } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { SocketService } from '../../shared/util/services/socket.service';
import { Message } from '../../shared/model/message';
import { Participant } from '../../shared/model/participantInfo';

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
  private possibleParticipants : Participant[] = null;
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
      }else{
        this.authenticated = false;
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
      }else{
        this.authenticated = false;
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

  getMessages(participant){
    this.messageInfo.active = participant;
    this.messageInfo.sendText = "";
    const msg = new Message('getMessages', JSON.stringify(participant), localStorage.getItem('logovanKorisnik'));
    this.sendMessage(msg);
  }

  getFriends(){
    const msg = new Message('getFriends', null, localStorage.getItem('logovanKorisnik'));
    this.sendMessage(msg);
  }

  getMyGroups(){
    const msg = new Message('getCreatedGroups', null, localStorage.getItem('logovanKorisnik'));
    this.sendMessage(msg);
  }

  getAddedGroups(){
    const msg = new Message('getGroupsAddedIn', null, localStorage.getItem('logovanKorisnik'));
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
      }else if(resp.messageType==="getFriends"){
        this.possibleParticipants = [];
        var friends = JSON.parse(resp.content);
        for(let f of friends){
          var found = false;
          for(let p of this.participants){
            if(p.participant!=null && p.participant===f.userName){
              found = true;
              break;
            }
          }
          if(!found){
            var pp = new Participant(f.userName, null, null, null);
            this.possibleParticipants.push(pp);
          }
        }
      }else if(resp.messageType==="getCreatedGroups" || resp.messageType==="getGroupsAddedIn"){
        this.possibleParticipants = [];
        var groups = JSON.parse(resp.content);
        for(let g of groups){
          var found = false;
          for(let p of this.participants){
            if(p.groupId!=null && p.groupId===g.groupId){
              found = true;
              break;
            }
          }
          if(!found){
            if(resp.messageType==="getGroupsAddedIn"){
              g.groupMembersList.push(g.parentUserId);
              const index : number = g.groupMembersList.indexOf(resp.loggedUserName);
              if(index!==-1){
                g.groupMembersList.splice(index,1);
              }
            }
              var pp = new Participant(null, g.groupMembersList, g.groupId, g.groupName);
              this.possibleParticipants.push(pp);
          }
        }
      }
    }
  }

  messageParticipant = function(pp){
    const index : number = this.possibleParticipants.indexOf(pp);
    if(index!==-1){
      this.possibleParticipants.splice(index,1);
    }
    this.participants.unshift(pp);
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
