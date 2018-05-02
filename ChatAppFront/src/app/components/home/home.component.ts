import { Component, OnInit } from '@angular/core';
import { Subject } from 'rxjs/Subject';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  private logovan : string;
  private authenticated : boolean = false;
  private participants : any;

  constructor() { }

  ngOnInit() {
    HomeComponent.updateUserStatus.subscribe(res => {
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

  public static updateUserStatus: Subject<boolean> = new Subject();

}
