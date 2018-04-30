import {Component, Input, OnInit} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Subject } from 'rxjs/Subject';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  @Input()
  private authenticated: boolean = false;
  private logovan : string;

  constructor() {
  }

  ngOnInit() {
    HeaderComponent.updateUserStatus.subscribe(res => {
      this.logovan = localStorage.getItem('logovanKorisnik');
      if(this.logovan!=null){
        this.authenticated = true;
      }
    })
    this.logovan = localStorage.getItem('logovanKorisnik');
    if(this.logovan!=null){
      this.authenticated = true;
    }

  }
  
  logout(ev) {
  }

  public static updateUserStatus: Subject<boolean> = new Subject();

}
