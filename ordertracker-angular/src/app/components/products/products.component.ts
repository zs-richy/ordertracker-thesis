import {Component, OnInit, ViewChild} from '@angular/core';
import {Product, ProductControllerService, ProductsDto} from "../../api/ordertracker-backend";
import {MatDialog} from "@angular/material/dialog";
import {DialogType, ProductDialogComponent, ProductDialogResult} from "../product-dialog/product-dialog.component";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ThemePalette} from "@angular/material/core";

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit {

  constructor(private _productsService: ProductControllerService,
              private _matDialog: MatDialog,
              private _matSnackBar: MatSnackBar) { }

  products?: Array<Product> = Array()
  productDataSource = new MatTableDataSource<Product>()
  displayedColumns = ["id", "name", "price", "actions"]

  ngOnInit(): void {
    this.initProductsData()
  }

  @ViewChild(MatPaginator) paginator?: MatPaginator;

  private initProductsData() {
    this._productsService.getProducts({imageType: "THUMBNAIL"}).subscribe(response => {
      console.log(response)
      this.products = response.productList?.sort((a, b) => {
        if (b.id ==  null) {
          return 1;
        } else if (a.id == null) {
          return -1
        } else {
          return  a.id - b.id
        }
      })
      if (this.products != null) {
        this.productDataSource.data = this.products
        // @ts-ignore
        this.productDataSource.paginator = this.paginator
      }
    }, error => {
      console.log(error)
      console.log(error.error.errorMessages)
      console.log((error.error as ProductsDto).errorMessages)
    })
  }

  productClick(product: Product) {
    this.openDialog(product, DialogType.VIEW)
  }

  editClicked(product: Product) {
    this.openDialog(product, DialogType.EDIT)
  }

  onAddClick() {
    this.openDialog({}, DialogType.NEW)
  }

  onRefreshClick() {
    this.initProductsData()
  }

  changeProductVisibility(product: Product) {
    if (product.id != undefined) {
      this._productsService.changeVisibilityById(product.id.toString()).subscribe(value =>
      {
        this.onRefreshClick()
      }, error =>
      {
        this._matSnackBar.open("Failed to change product visibility", "OK", {duration: 3000})
      })
    }
  }

  openDialog(product: Product, dialogType: DialogType): void {
    const dialogRef = this._matDialog.open(ProductDialogComponent, {
      width: '35vw',
      minWidth: '750px',
      data: {product: product, type: dialogType}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.handleDialogResult(result)
    });
  }

  handleDialogResult(result: ProductDialogResult) {
    if (result && result.product != null && (result.type == DialogType.NEW || result.type == DialogType.EDIT) && this.products != null) {
      this.initProductsData()
    }
  }

  productVisibilityColor(product: Product): ThemePalette {
    if (product.isVisible) {
      return "primary"
    } else {
      return "warn"
    }
  }

}
