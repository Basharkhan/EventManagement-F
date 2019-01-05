import { Component } from '@angular/core';
import { AuthserviceService } from './services/authservice.service';
import { TokenStorageServiceService } from './services/token-storage-service.service';
import { User } from './user';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { Router, NavigationStart, NavigationEnd, NavigationError, NavigationCancel, Event } from '@angular/router';
const TOKEN_KEY = 'AuthToken';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  currentUser: string;
  showLoadingIndicator = true;


  constructor(private token: TokenStorageServiceService, private _router: Router) {
    //this.currentUser = JSON.parse(sessionStorage.getItem(TOKEN_KEY));
    this.currentUser = this.token.getUsername();
    this._router.events.subscribe((routerEvent: Event) => {
      if (routerEvent instanceof NavigationStart) {
        this.showLoadingIndicator = true;
        console.log(this.showLoadingIndicator)
      }

      if (routerEvent instanceof NavigationEnd ||
        routerEvent instanceof NavigationError ||
        routerEvent instanceof NavigationCancel) {
        this.showLoadingIndicator = false;
        console.log(this.showLoadingIndicator)
      }
    });
  }

}
