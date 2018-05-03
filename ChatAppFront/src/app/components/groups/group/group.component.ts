import { Component, OnInit } from '@angular/core';
import { HelperFunctions } from '../../../shared/util/helper-functions';
import { ActivatedRoute } from '@angular/router';
import { SocketService } from '../../../shared/util/services/socket.service';
import { Message } from '../../../shared/model/message';

@Component({
  selector: 'app-group',
  templateUrl: './group.component.html',
  styleUrls: ['./group.component.css']
})
export class GroupComponent implements OnInit {
  
  private loggedUserName = localStorage.getItem('logovanKorisnik');
  private removableUsers: any[];
  private addableUsers: any[];
  private groupInfo: any;
  private groupName: string;
  private groupId: number;

  constructor(private route: ActivatedRoute, private socketService: SocketService) { 
    this.route.params.subscribe(params => this.groupId = params.id);
  }

  ngOnInit() {
    const info = {
      groupId: this.groupId,
      username: this.loggedUserName
    };
    const msg = new Message('getAddableUsers', JSON.stringify(info), this.loggedUserName);
    const grpmsg = new Message('getOneGroup', this.groupId, this.loggedUserName);
    if(this.socketService.socket.readyState === this.socketService.socket.OPEN) {
      this.socketService.send(msg);
      this.socketService.send(grpmsg);
    } else {
      this.socketService.socket.onopen = (event) => {
        this.socketService.send(msg);
        this.socketService.send(grpmsg);
      }
    }

    this.socketService.socket.onmessage = (event) => {
      const data = event.data;
      const parsed = JSON.parse(data);
      if(parsed.messageType === 'getAddableUsers') {
        console.log(parsed.content);
        this.addableUsers = JSON.parse(parsed.content);
      } else if(parsed.messageType === 'getOneGroup') {
        console.log(parsed.content);
        this.groupInfo = JSON.parse(parsed.content);
        this.removableUsers = this.groupInfo.groupMembersList;
      }
    }

    if(!HelperFunctions.isEmptyValue(this.groupInfo))
      this.groupName = this.groupInfo.groupName;
  }

  addUser(user: any) {
    const addInfo = {
      groupId: this.groupId,
      userName: user.userName
    };

    this.socketService.send(new Message('addToGroup', JSON.stringify(addInfo), this.loggedUserName));
    this.socketService.socket.onmessage = (event) => {
      if(HelperFunctions.isEmptyValue(this.removableUsers)) {
        this.removableUsers = [];
      }

      if(this.removableUsers.indexOf(user.username) === -1) {
        this.removableUsers.push(user.userName);
        HelperFunctions.deleteItemFromArray(this.addableUsers, user.username);
      }
    }
  }

  kickUser(username: any) {
    const leaveGrpDto = {
      kickedBy: this.loggedUserName,
      leaverUsername: username,
      groupId: this.groupId
    };

    this.socketService.send(new Message('leaveKickFromGroup', JSON.stringify(leaveGrpDto), this.loggedUserName));
    this.socketService.socket.onmessage = (event) => {
      if(HelperFunctions.isEmptyValue(this.addableUsers)) {
        this.addableUsers = [];
      }

      if(this.addableUsers.indexOf(username) === -1) {
        this.addableUsers.push(username);
        HelperFunctions.deleteItemFromArray(this.removableUsers, username);
      }
    }
  }

  convertToAddable() {
    return HelperFunctions.createListItems(this.addableUsers, null, ['userName']);
  }

  convertToKickable() {
    return HelperFunctions.createListItems(this.removableUsers, null, null);
  }
}
