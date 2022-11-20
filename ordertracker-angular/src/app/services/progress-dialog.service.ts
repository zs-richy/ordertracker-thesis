import { Injectable } from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {ProgressDialogComponent} from "../components/progress-dialog/progress-dialog.component";
import {MatDialogRef} from "@angular/material/dialog/dialog-ref";

@Injectable({
  providedIn: 'root'
})
export class ProgressDialogService {

  constructor(private _matDialog: MatDialog) { }

  dialogRef?: MatDialogRef<any>

  openDialog(): void {
    this.dialogRef?.close()
    this.dialogRef = this._matDialog.open(ProgressDialogComponent, {
      width: '35vw',
      minWidth: '750px',
    });
  }

  closeDialog(): void {
    this.dialogRef?.close()
  }

}
