import { Component, OnInit } from '@angular/core';
import { SocketService } from '../../shared/util/services/socket.service';
import { Message } from '../../shared/model/message';
import {HelperFunctions} from '../../shared/util/helper-functions';
import { PageMessage } from '../../shared/model/page-message';
import { Constants } from '../../shared/constants/constants';


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
  private groupInfo = {
    groupName: null,
    parentUserId: this.loggedUserName,
    groupMemberList: null
  };

  constructor(private socketService: SocketService) {

   }

  ngOnInit() {
    const getFriendsMsg = new Message('getFriends', null, this.loggedUserName);
    const getGroupsMsg = new Message('getGroups', this.loggedUserName, this.loggedUserName);

    this.socketService.send(getFriendsMsg);
    this.socketService.send(getGroupsMsg);
    
    this.socketService.socket.onmessage = (event) => {
      const ret = event.data;
      if(!HelperFunctions.isEmptyValue(ret)) {
        const parsed = JSON.parse(ret);

        if(parsed.messageType === 'getFriends') {
          console.log("Friends");
          this.friends = HelperFunctions.createListItems(JSON.parse(parsed.content), null, ['userName']);
          console.log(this.friends);
        } else if(parsed.messageType === 'getGroups') {
          console.log("Groups");
          this.groups = JSON.parse(parsed.content);
          console.log(this.groups);
        }
      }
    }
  }

  createGroup() {
    if(this.canCreate()) {
      this.socketService.send(new Message('createGroup', JSON.stringify(this.groupInfo)
                              , this.loggedUserName));
      this.socketService.socket.onerror = (event) => {
        const data = event.returnValue;
        console.log(data);
        console.log(event);
      } 
    } else {
      this.message = new PageMessage(Constants.messageType.ERR, 'Error creating group');
      console.log(this.message);
    }
  }

  canCreate() {
    return !HelperFunctions.isEmptyValue(this.groupInfo);
  }

  addUserToGroup(user: any) {
    console.log(user.userName);
    if(HelperFunctions.isEmptyValue(this.groupInfo.groupMemberList)) {
      this.groupInfo.groupMemberList = [];
    }
    if(this.groupInfo.groupMemberList.indexOf(user.userName) === -1)
      this.groupInfo.groupMemberList.push(user.userName);
  }
}
