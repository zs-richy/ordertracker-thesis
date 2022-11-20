import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {
  FindProductByIdDto,
  ImageControllerService,
  Product,
  ProductControllerService
} from "../../api/ordertracker-backend";
import {DomSanitizer} from "@angular/platform-browser";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ProgressDialogService} from "../../services/progress-dialog.service";

export interface ProductDialogData {
  product: Product,
  type: DialogType
}

export interface ProductDialogResult {
  product?: Product
  type: DialogType
}

@Component({
  selector: 'app-product-dialog',
  templateUrl: './product-dialog.component.html',
  styleUrls: ['./product-dialog.component.scss']
})
export class ProductDialogComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<ProductDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ProductDialogData,
    private _sanitizer: DomSanitizer,
    private _productService: ProductControllerService,
    private _imageService: ImageControllerService,
    private _matSnackBar: MatSnackBar,
    private _progressDialog: ProgressDialogService
  ) {
  }

  originalProduct?: Product;
  product?: Product;
  productImage?: string
  originalProductImage?: string
  dialogType: DialogType = DialogType.VIEW

  ngOnInit(): void {
    if (this.data.product != null) {
      this.product = this.data.product
      this.originalProduct = Object.assign({}, this.data.product)

      if (this.data.type != DialogType.NEW) {
        let findByIdDto: FindProductByIdDto = {id: this.product.id, withBinary: true}
        this._productService.findProductById(findByIdDto)
          .subscribe(response => {
            if (response.images != null) {
              response.images.forEach(image => {
                if (image.type == "THUMBNAIL") {
                  this.productImage = image.imageData
                  this.originalProductImage = image.imageData
                  console.log(image.imageData)
                }
              })
            }
          })
      }

    }

    if (this.data.type != null) {
      this.dialogType = this.data.type
    }

  }

  loadBase64Image() {
    if (this.productImage != null) {
      return this._sanitizer.bypassSecurityTrustResourceUrl('data:image/png;base64,' + this.productImage);
    }
    return null
  }

  convertTypeVisibility() {
    return this.dialogType == DialogType.VIEW
  }

  hideImageSpinner() {
    return (this.product?.images != null && this.product.images.length > 0 && this.productImage != null)
      || this.product?.images?.length == 0
      || this.product?.images == null
  }

  hideImageEditButton() {
    return this.product?.images == null || this.product.images.length == 0
  }

  hideImageUploadButton() {
    return this.product?.images != null && this.product.images.length > 0
  }

  onCancelClick(): void {
    this.product = Object.assign(this.product, this.originalProduct)
    this.dialogRef.close();
  }

  onOkClick(): void {
    switch (this.dialogType) {
      case DialogType.VIEW:
        this.dialogRef.close();
        break;
      case DialogType.EDIT:
        this.modifyProduct();
        break;
      case DialogType.NEW:
        this.newProduct();
        break;
      default:
        this.dialogRef.close()
    }
  }

  async modifyProduct() {
    this._progressDialog.openDialog()
    let productSaved = false
    let imageSaved = false

    if (this.product != null) {
      let productResponse = await this._productService.saveProduct(this.product).toPromise()
      if (productResponse.id != null) {
        productSaved = true
      }
    }

    if (this.originalProductImage != this.productImage && this.productImage != null) {
      let imageResponse = await this._imageService.addImage({imageData: this.productImage, productId: this.product?.id, id: 0}).toPromise()
      imageSaved = true
    } else {
      imageSaved = true
    }

    if (productSaved && imageSaved) {
      this._matSnackBar.open("Product saved", "OK", {duration: 3000})
      let dialogResult: ProductDialogResult = {
        product: this.product,
        type: DialogType.EDIT
      }
      this.dialogRef.close(dialogResult)
    }

    this._progressDialog.closeDialog()

  }

  async newProduct() {
    let productSaved = false
    let imageSaved = false

    if (this.product != null) {
      let productResponse = await this._productService.addProduct(this.product).toPromise()

      if (productResponse.id != null) {
        this.product = productResponse
        productSaved = true
      }
    }

    if (this.originalProductImage != this.productImage && this.productImage != null) {
      let imageResponse = await this._imageService.addImage({imageData: this.productImage, productId: this.product?.id, id: 0}).toPromise()
      imageSaved = true
    } else {
      imageSaved = true
    }

    if (productSaved && imageSaved) {
      this._matSnackBar.open("Product created", "OK", {duration: 3000})

      let dialogResult: ProductDialogResult = {
        product: this.product,
        type: DialogType.NEW
      }
      this.dialogRef.close(dialogResult)
    }

  }

  handleImageInputChange(event: any) {
    const file = event.target.files[0];
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      if (typeof reader.result === "string") {
        this.productImage = reader.result.split(',')[1];
        console.log(this.productImage)
      }
    };
  }

}

export enum DialogType {
  VIEW,
  EDIT,
  NEW
}
