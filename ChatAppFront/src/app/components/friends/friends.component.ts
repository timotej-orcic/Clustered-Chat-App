import { Component, OnInit } from '@angular/core';
import { SocketService } from '../../shared/util/services/socket.service';

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

}
