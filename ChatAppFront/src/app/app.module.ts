import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './/app-routing.module';
import { FormsModule } from '@angular/forms';

import { HeaderComponent } from './components/header/header.component';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { SocketService } from './shared/util/services/socket.service';
import { FriendsComponent } from './components/friends/friends.component';
import { GroupsComponent } from './components/groups/groups.component';
import { GroupComponent } from './components/groups/group/group.component';
import { MessagingComponent } from './shared/components/messaging/messaging.component'
import { SearchComponent } from './shared/components/search/search.component';
import { ListComponent } from './shared/components/list/list.component';
import { RequestComponent } from './shared/components/request/request.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    HeaderComponent,
    LoginComponent,
    RegistrationComponent,
    FriendsComponent,
    GroupsComponent,
    GroupComponent,
    MessagingComponent,
    SearchComponent,
    ListComponent,
    RequestComponent
  ],
  exports: [
    HeaderComponent,
    HomeComponent,
    LoginComponent,
    RegistrationComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule
  ],
  providers: [SocketService],
  bootstrap: [AppComponent]
})
export class AppModule { }
