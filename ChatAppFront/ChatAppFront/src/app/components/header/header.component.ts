import {Component, Input, OnInit} from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  @Input()
  private authenticated: boolean;

  constructor() {
    this.authenticated = false;
  }

  ngOnInit() {
  }

  logout(ev) {
  }

}
