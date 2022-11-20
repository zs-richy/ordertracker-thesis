import { Component, OnInit } from '@angular/core';
import {SessionService} from "../../services/session/session.service";
import {Router} from "@angular/router";
import {Product} from "../../api/ordertracker-backend";
import {DialogType, ProductDialogComponent} from "../product-dialog/product-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {MotdDialogComponent} from "../motd-dialog/motd-dialog.component";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(public sessionService: SessionService,
              private _matDialog: MatDialog,
              private _router: Router) { }

  ngOnInit(): void {
  }

  moveToPage(page: String) {
    this._router.navigate([`/${page}`])
  }

  openMotdDialog() {
    this._matDialog.open(MotdDialogComponent, {
      width: '35vw',
      minWidth: '750px',
      disableClose: true
    });
  }

}
