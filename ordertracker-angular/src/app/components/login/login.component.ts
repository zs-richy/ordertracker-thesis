import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import {AuthControllerService, LoginRequest} from "../../api/ordertracker-backend";
import {SessionService} from "../../services/session/session.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  username: string = ""
  password: string = ""

  callInProgress = false

  constructor(private authService: AuthControllerService,
              private snackBar: MatSnackBar,
              private sessionService: SessionService,
              private router: Router) { }

  ngOnInit(): void {
  }

  login() {
    this.callInProgress = true
    this.authService.authenticateUser(
      {username: this.username, password: this.password},
    ).subscribe(response => {
        if (response.token == null) {
          this.snackBar.open("Authentication failed", "OK")
          return
        }

        if (!response.roles?.includes("ADMIN")) {
          this.snackBar.open("User not admin!", "OK")
          return
        }

        this.sessionService.setSessionData(response)
        this.router.navigate(["/home"])
      }, error => {
        this.snackBar.open("Authentication failed", "OK")
      }).add(() => {
        this.callInProgress = false
    })
  }

}
