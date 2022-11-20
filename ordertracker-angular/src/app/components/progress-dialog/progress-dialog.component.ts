import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ProductDialogData} from "../product-dialog/product-dialog.component";

export interface ProgressDialogData {
  title: string,
  content: string
}

@Component({
  selector: 'app-progress-dialog',
  templateUrl: './progress-dialog.component.html',
  styleUrls: ['./progress-dialog.component.scss']
})
export class ProgressDialogComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: ProgressDialogData,
              private _dialogRef: MatDialogRef<ProgressDialogComponent>) {
    this._dialogRef.disableClose = true
  }

  title = "In progress"
  content = "Please wait..."

  ngOnInit(): void {
    if (this.data != null) {
      this.title = this.data.title
      this.content = this.data.content
    }
  }

}
