import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {HelperFunctions} from '../../util/helper-functions';
import {Constants} from '../../constants/constants';
import {ListItem} from '../../model/list-item';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit {

  private listType = Constants.ListType;
  private requestType = Constants.RequestType;
  @Output() onElementClickEvent: EventEmitter<any> = new EventEmitter();
  @Input() public header: string;
  @Input() public items: any;
  @Input() public type: string;
  @Output() acceptClickEvent: EventEmitter<any> = new EventEmitter<any>();
  @Output() declineClickEvent: EventEmitter<any> = new EventEmitter<any>();

  constructor() {
    console.log(this.items);
  }

  ngOnInit() {}

  elementClicked(item) {
    this.onElementClickEvent.emit(item);
  }

  accept(object) {
    this.acceptClickEvent.emit(object);
  }

  decline(object) {
    this.declineClickEvent.emit(object);
  }
}
