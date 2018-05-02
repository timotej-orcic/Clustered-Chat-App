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
  searchFriendsInfo = {
    userName: '',
    name: '',
    lastName : ''
  };
  searchUsersInfo = {
    userName: '',
    name: '',
    lastName : ''
  };

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
    const msg = new Message('addFriend', friend.userName, localStorage.getItem('logovanKorisnik'));
    this.sendMessage(msg);
  }

  deleteFriend = function(friend){
    const msg = new Message('deleteFriend', friend.userName, localStorage.getItem('logovanKorisnik'));
    this.sendMessage(msg);
  }

  searchFriend = function(){
    const msg = new Message('searchFriends', JSON.stringify(this.searchFriendsInfo), localStorage.getItem('logovanKorisnik'));
    this.sendMessage(msg);
  }

  refreshFriend = function(){
    this.searchFriendsInfo.userName = "";
    this.searchFriendsInfo.name  = "";
    this.searchFriendsInfo.lastName  = "";
    this.getFriends();
  }

  searchUser = function(){
    const msg = new Message('searchNonFriends', JSON.stringify(this.searchUsersInfo), localStorage.getItem('logovanKorisnik'));
    this.sendMessage(msg);
  }

  refreshUser = function(){
    this.searchUsersInfo.userName = "";
    this.searchUsersInfo.name  = "";
    this.searchUsersInfo.lastName  = "";
    this.getNonFriends();
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
      }else if(resp.messageType==="getNonFriends"){
        this.userList = JSON.parse(resp.content);
      }else if(resp.messageType==="addFriend"){
        if(resp.content!=null){
          var index = 0;
          for(let user of this.userList){
            if(user.userName===resp.content){
              this.userList.splice(index,1);
            }
            index = index+1;
          }
        }
      }else if(resp.messageType==="searchFriends"){
        this.friendsList = JSON.parse(resp.content);
      }else if(resp.messageType==="searchNonFriends"){
        this.userList = JSON.parse(resp.content);
      }
    };

  }

}
