import {Component} from '@angular/core';
import {SessionService} from "./services/session/session.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'ordertrack-angular';

  constructor(public sessionService: SessionService,
              private router: Router) {
  }

  onTitleClick() {
    this.router.navigate(["/"])
  }

  onProductsClick() {
    this.router.navigate(["/products"])
  }

  onOrdersClick() {
    this.router.navigate(["/orders"])
  }

}
