import { Component, OnInit } from '@angular/core';
import { HelperFunctions } from '../../../shared/util/helper-functions';

@Component({
  selector: 'app-group',
  templateUrl: './group.component.html',
  styleUrls: ['./group.component.css']
})
export class GroupComponent implements OnInit {
  
  private loggedUserName = localStorage.getItem('logovanKorisnik');
  private groupInfo: any;

  constructor() { }

  ngOnInit() {
  }

  addUserToGroup(user: any) {
    console.log(user.userName);
    if(HelperFunctions.isEmptyValue(this.groupInfo.groupMemberList)) {
      this.groupInfo.groupMemberList = [];
    }
    if(this.groupInfo.groupMemberList.indexOf(user.userName) === -1) {
      this.groupInfo.groupMemberList.push(user.userName);
    } else {
      this.groupInfo.groupMemberList.split(this.groupInfo.groupMemberList.indexOf(user), 1);
    }
  }
}
