import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginComponent} from './components/login/login.component';
import {HomeComponent} from './components/home/home.component';
import {MatButtonModule} from '@angular/material/button';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatInputModule} from '@angular/material/input';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {
  AuthControllerService, GeneralControllerService,
  ImageControllerService,
  OrderControllerService,
  ProductControllerService
} from "./api/ordertracker-backend";
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from "@angular/common/http";
import {SessionService} from "./services/session/session.service";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatCardModule} from "@angular/material/card";
import {ProductsComponent} from './components/products/products.component';
import {CustomHttpInterceptor} from "./services/custom.http.interceptor";
import {MatTableModule} from "@angular/material/table";
import {MatIconModule} from "@angular/material/icon";
import {ProductDialogComponent} from './components/product-dialog/product-dialog.component';
import {MatDialogModule} from "@angular/material/dialog";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatPaginatorModule} from "@angular/material/paginator";
import {ProgressDialogComponent} from './components/progress-dialog/progress-dialog.component';
import {OrdersComponent} from './components/orders/orders.component';
import {OrderComponent} from './components/order/order.component';
import {MatListModule} from "@angular/material/list";
import {MatSelectModule} from "@angular/material/select";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MAT_DATE_LOCALE, MatNativeDateModule} from "@angular/material/core";
import { MotdDialogComponent } from './components/motd-dialog/motd-dialog.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    ProductsComponent,
    ProductDialogComponent,
    ProgressDialogComponent,
    OrdersComponent,
    OrderComponent,
    MotdDialogComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatInputModule,
    FormsModule,
    MatSnackBarModule,
    MatToolbarModule,
    MatCardModule,
    MatTableModule,
    MatIconModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    MatPaginatorModule,
    MatListModule,
    MatSelectModule,
    MatCheckboxModule,
    MatDatepickerModule,
    MatNativeDateModule,
    ReactiveFormsModule
  ],
  providers: [
    AuthControllerService,
    ProductControllerService,
    ImageControllerService,
    OrderControllerService,
    GeneralControllerService,
    HttpClient,
    SessionService,
    {provide: HTTP_INTERCEPTORS, useClass: CustomHttpInterceptor, multi: true},
    {provide: MAT_DATE_LOCALE, useValue: 'en-GB'}
],
  bootstrap: [AppComponent]
})
export class AppModule {
}
