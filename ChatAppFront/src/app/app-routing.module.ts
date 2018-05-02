import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { FriendsComponent } from './components/friends/friends.component';
import { GroupsComponent } from './components/groups/groups.component';
import { GroupComponent } from './components/groups/group/group.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegistrationComponent
  },
  {
    path : 'friends',
    component : FriendsComponent
  },
  {
    path: 'groups',
    component : GroupsComponent
  },
  {
    path: 'groups/:id',
    component : GroupComponent
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule { }
