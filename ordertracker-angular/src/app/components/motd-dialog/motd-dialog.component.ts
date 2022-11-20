import { Component, OnInit } from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {GeneralControllerService, WelcomeDto} from "../../api/ordertracker-backend";
import {DomSanitizer} from "@angular/platform-browser";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-motd-dialog',
  templateUrl: './motd-dialog.component.html',
  styleUrls: ['./motd-dialog.component.scss']
})
export class MotdDialogComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<MotdDialogComponent>,
    private sanitizer: DomSanitizer,
    private generalControllerService: GeneralControllerService,
    private matSnackBar: MatSnackBar
  ) { }

  welcomeDto: WelcomeDto = { imageData: undefined, message: ""}
  welcomeImage?: string
  isUpdating = false

  ngOnInit(): void {
    this.isUpdating = true
    this.generalControllerService.getWelcome().subscribe(response => {
      this.welcomeDto = response
      this.welcomeImage = response.imageData
    }, error => {
      this.matSnackBar.open("Failed to load 'Message of the day' data",
        "OK", {duration: 1500})
    }).add(() => {
      this.isUpdating = false
    })
  }

  onCancelClick(): void {
    this.dialogRef.close();
  }

  onUpdateClick() {
    this.isUpdating = true
    this.welcomeDto.imageData = this.welcomeImage
    this.generalControllerService.setWelcome(this.welcomeDto).subscribe(value => {
      this.isUpdating = false
      this.dialogRef.close()
    }, error => {
      this.matSnackBar.open("Failed to update 'Message of the day' data", "OK",
        {duration: 1500})
    })
  }

  loadBase64Image() {
    if (this.welcomeImage != null) {
      return this.sanitizer.bypassSecurityTrustResourceUrl('data:image/png;base64,' + this.welcomeImage);
    }
    return null
  }

  handleImageInputChange(event: any) {
    const file = event.target.files[0];
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      if (typeof reader.result === "string") {
        this.welcomeImage = reader.result.split(',')[1];
        console.log(this.welcomeImage)
      }
    };
  }

}
