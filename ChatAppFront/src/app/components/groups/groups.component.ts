import { Component, OnInit, ViewChild } from '@angular/core';
import { SocketService } from '../../shared/util/services/socket.service';
import { Message } from '../../shared/model/message';
import {HelperFunctions} from '../../shared/util/helper-functions';
import { PageMessage } from '../../shared/model/page-message';
import { Constants } from '../../shared/constants/constants';
import { Router } from '@angular/router';
import { ListComponent } from '../../shared/components/list/list.component';


@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.css']
})
export class GroupsComponent implements OnInit {

  private loggedUserName = localStorage.getItem('logovanKorisnik');
  private showGroupCreation = false;
  private message: PageMessage;
  private friends = [];
  private groups = [];
  private unrelatedGroups = [];
  private relatedGroups = [];
  private groupInfo = {
    groupName: null,
    parentUserId: this.loggedUserName,
    groupMemberList: []
  };
  @ViewChild('friendlist') friendList: ListComponent

  constructor(private socketService: SocketService, private router: Router) {

  }

  ngOnInit() {
    const getFriendsMsg = new Message('getFriends', null, this.loggedUserName);
    const getGroupsMsg = new Message('getGroups', this.loggedUserName, this.loggedUserName);
    const getRelatedGroups = new Message('getCreatedGroups', this.loggedUserName, this.loggedUserName);
    const getUnrelatedGroups = new Message('getGroupsAddedIn', this.loggedUserName, this.loggedUserName);

    if(this.socketService.socket.readyState === this.socketService.socket.OPEN) {
      this.socketService.send(getFriendsMsg);
      this.socketService.send(getGroupsMsg);
    }

    this.socketService.socket.onopen = (event) => {
      this.socketService.send(getFriendsMsg);
      this.socketService.send(getGroupsMsg);
      this.socketService.send(getRelatedGroups);
      this.socketService.send(getUnrelatedGroups);
    }
    
    this.socketService.socket.onmessage = (event) => {
      const ret = event.data;
      if(!HelperFunctions.isEmptyValue(ret)) {
        const parsed = JSON.parse(ret);
        if(parsed.messageType === 'getFriends') {
          this.friends = HelperFunctions.createRequestItems(JSON.parse(parsed.content), 
                                        ['userName'], null, null, Constants.RequestType.ADD_REMOVE);
          console.log(this.friends);
        } else if(parsed.messageType === 'getGroups') {
          this.groups = JSON.parse(parsed.content);
          console.log(this.groups);
        } else if(parsed.messageType === 'getCreatedGroups') {
          this.relatedGroups =JSON.parse(parsed.content);
        } else if(parsed.messageType === 'getGroupsAddedIn') {
          this.unrelatedGroups = JSON.parse(parsed.content);
        } else if(parsed.messageType === 'createGroup') {
          this.relatedGroups.push(JSON.parse(parsed.content));
        } else if(parsed.messageType === 'getGroupsAddedIn') {
          this.unrelatedGroups = HelperFunctions.createListItems(JSON.parse(parsed.content),
                                                 null, ['name']);
        }
      }
    }
  }

  createGroup() {
    if(this.canCreate()) {
      this.socketService.send(new Message('createGroup', JSON.stringify(this.groupInfo)
                              , this.loggedUserName));
      this.socketService.socket.onmessage = (event) => {
        const data = event.data;
        const msg = JSON.parse(data);
        if(!HelperFunctions.isEmptyValue(this.relatedGroups)) {
          this.relatedGroups.push(JSON.parse(msg.content));
          this.groupInfo.groupName = '';
          this.groupInfo.groupMemberList = [];
          this.friendList.resetChildren();
        }
      } 
    } else {
      this.message = new PageMessage(Constants.messageType.ERR, 'Error creating group');
      console.log(this.message);
    }
  }

  deleteGroup(group) {
    this.socketService.send(new Message('deleteGroup', group.groupId, this.loggedUserName))

    this.socketService.socket.onmessage = (event) => {
      const ret = event.data;
      if(!HelperFunctions.isEmptyValue(ret)) {
        const parsed = JSON.parse(ret);

        if(parsed.messageType === 'deleteGroup') {
          this.relatedGroups.splice(this.relatedGroups.indexOf(group), 1);
          console.log(this.groups);
        } else if(parsed.messageType === 'fail') {
          console.log("Fail: " + parsed.content);
          this.message = new PageMessage(Constants.messageType.ERR, parsed.message);
        }
      }
    }
  }

  leaveGroup(group) {
    const leaveGrpDto = {
      kickedBy: null,
      leaverUsername: this.loggedUserName,
      groupId: group.groupId
    };

    this.socketService.send(new Message('leaveKickFromGroup', JSON.stringify(leaveGrpDto), this.loggedUserName));
   
    this.socketService.socket.onmessage = (event) => {
      const ret = event.data;
      if(!HelperFunctions.isEmptyValue(ret)) {
        const parsed = JSON.parse(ret);

        if(parsed.messageType === 'leaveKickFromGroup') {
          HelperFunctions.deleteItemFromArray(this.unrelatedGroups, group);
          console.log(this.groups);
        } else if(parsed.messageType === 'fail') {
          console.log("Fail: " + parsed.content);
          this.message = new PageMessage(Constants.messageType.ERR, parsed.message);
        }
      }
    }
  }

  addUserToGroup(user: any) {
    console.log(user.userName);
    if(HelperFunctions.isEmptyValue(this.groupInfo.groupMemberList)) {
      this.groupInfo.groupMemberList = [];
    }
    if(this.groupInfo.groupMemberList.indexOf(user.userName) === -1) {
      this.groupInfo.groupMemberList.push(user.userName);
    } else {
      this.groupInfo.groupMemberList.splice(this.groupInfo.groupMemberList.indexOf(user), 1);
    }
  }

  canCreate() {
    return !HelperFunctions.isEmptyValue(this.groupInfo);
  }

  enterGroup(group) {
    console.log("ENTER GROUP");
    console.log(group);
    this.router.navigate(['/' + group.groupId]);
  }
}
