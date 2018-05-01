import { Component, OnInit } from '@angular/core';
import { SocketService } from '../../shared/util/services/socket.service';
import { Message } from '../../shared/model/message';


@Component({
  selector: 'app-friends',
  templateUrl: './friends.component.html',
  styleUrls: ['./friends.component.css']
})
export class FriendsComponent implements OnInit {

  constructor(private socketService : SocketService) { }

  friendsList : any;
  userList : any;
  friendUserName : string = "";
  friendName : string = "";
  friendLastName : string = "";
  userUserName : string = "";
  userName : string = "";
  userLastName : string = "";

  ngOnInit() {
    this.getFriends();
  }

  getFriends = function(){
    const msg = new Message('getFriends', null, localStorage.getItem('logovanKorisnik'));
    this.sendMessage(msg);

  }

  getNonFriends = function(){
    const msg = new Message('getNonFriends', null, localStorage.getItem('logovanKorisnik'));
    this.sendMessage(msg);
  }

  addFriend = function(friend){
    console.log("Friend added");
  }

  deleteFriend = function(friend){
    const msg = new Message('deleteFriend', friend.userName, localStorage.getItem('logovanKorisnik'));
    this.sendMessage(msg);
  }

  searchFriend = function(){
    console.log(this.friendUserName + " " + this.friendName + " "+ this.friendLastName);
  }

  refreshFriend = function(){
    this.friendUserName = "";
    this.friendName = "";
    this.friendLastName = "";
    console.log('Friend refreshed');
  }

  searchUser = function(){
    console.log(this.userUserName + " " + this.userName + " "+ this.userLastName);
  }

  refreshUser = function(){
    this.userUserName = "";
    this.userName = "";
    this.userLastName = "";
    console.log('User refreshed');
  }

  public sendMessage(message: Message): void {
    if (!message) {
      return;
    }
    this.socketService.send(message);

    this.socketService.socket.onmessage = (event) => { 
      var resp = JSON.parse(event.data);
      if(resp.messageType==="getFriends"){
        this.friendsList = JSON.parse(resp.content);
      }else if(resp.messageType==="deleteFriend"){
        if(resp.content!=null){
          var index = 0;
          for(let friend of this.friendsList){
            if(friend.userName===resp.content){
              this.friendsList.splice(index,1);
            }
            index = index+1;
          }
        }
      }
    };


  }

}
