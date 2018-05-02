import { Component, OnInit, Input } from '@angular/core';
import { PageMessage } from '../../model/page-message';
import { HelperFunctions } from '../../util/helper-functions';

@Component({
  selector: 'app-messaging',
  templateUrl: './messaging.component.html',
  styleUrls: ['./messaging.component.css']
})
export class MessagingComponent implements OnInit {

  @Input()
  private message: PageMessage;
  private msg: string;
  private type: string;

  constructor() { }

  ngOnInit() {
    if(!HelperFunctions.containsEmptyValues(this.message)){
      this.type = this.message.type;
      this.msg = this.message.msg;
    }
  }

}
