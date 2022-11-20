import {Injectable} from '@angular/core';
import {LoginResponse} from "../../api/ordertracker-backend/model/loginResponse";

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  username?: string
  token?: string
  expires?: number
  roles?: Array<String>

  constructor() {
  }

  setSessionData(response: LoginResponse) {
    this.username = response.username
    this.token = response.token
    this.expires = response.expires
    this.roles = response.roles
  }

  isLoggedIn(): boolean {
    return this.token != null && (this.expires != null && new Date().getTime() < this.expires);
  }

  isAdmin(): Boolean {
    if (this.roles == null || !this.isLoggedIn()) {
      return false
    } else {
      return this.roles?.includes("ADMIN")
    }

  }

}
