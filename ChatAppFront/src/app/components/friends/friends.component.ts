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
    const msg = new Message('getFriends', null, "user1");
    this.sendMessage(msg);
  }

  getNonFriends = function(){
    const msg = new Message('getNonFriends', null, "user1");
    this.sendMessage(msg);
  }

  addFriend = function(friend){
    console.log("Friend added");
  }

  deleteFriend = function(friend){
    console.log("Friend deleted");
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
  }

}
