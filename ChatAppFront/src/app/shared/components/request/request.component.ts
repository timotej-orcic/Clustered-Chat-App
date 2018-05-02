import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Constants} from '../../constants/constants';

@Component({
  selector: 'app-request',
  templateUrl: './request.component.html',
  styleUrls: ['./request.component.css']
})
export class RequestComponent implements OnInit {

  public types = Constants.RequestType;
  @Input() private type: string;
  @Input() private requestText: string;
  @Input() relatedItem: any;
  @Output() acceptClickEvent: EventEmitter<any> = new EventEmitter<any>();
  @Output() declineClickEvent: EventEmitter<any> = new EventEmitter<any>();

  constructor() { }

  ngOnInit() {
    console.log(this.requestText);
    console.log(this.type);
  }

  accept(event) {
    if (this.relatedItem == null) {
      this.acceptClickEvent.emit(null);
    } else {
      this.acceptClickEvent.emit(this.relatedItem);
    }
  }

  decline(event) {
    if (this.relatedItem == null) {
      this.declineClickEvent.emit(null);
    } else {
      this.declineClickEvent.emit(this.relatedItem);
    }
  }
}
