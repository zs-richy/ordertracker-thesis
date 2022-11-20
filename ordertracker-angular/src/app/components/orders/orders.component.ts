import {Component, OnDestroy, OnInit} from '@angular/core';
import {Order, OrderControllerService} from "../../api/ordertracker-backend";
import {MatCheckboxChange} from "@angular/material/checkbox";
import StatusEnum = Order.StatusEnum;
import {MAT_DATE_LOCALE} from "@angular/material/core";
import {FormControl, FormGroup} from "@angular/forms";
import {interval, Observable} from "rxjs";


@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.scss']
})
export class OrdersComponent implements OnInit, OnDestroy {

  constructor(private _orderService: OrderControllerService) {
  }

  orders?: Array<Order>

  filtered?: Array<Order>

  displayCompleted: Boolean = false
  selectedDate = new FormControl(new Date());

  filterInterval = interval(5000).subscribe(value => {
    this.fetchOrdersByDate()
  })

  ngOnInit(): void {
    this.fetchOrdersByDate()
  }


  ngOnDestroy(): void {
    this.filterInterval.unsubscribe()
  }

  fetchOrdersByDate() {
    this._orderService.getOrdersByDate({date: this.formattedSelectedDate()}).subscribe(response => {
      this.orders = response.orders
      this.filterOrders()
      this.onFilterChange()
    })
  }

  formattedSelectedDate(): Date {
    console.log(this.selectedDate.value);
    (this.selectedDate.value as Date).setHours(12)
    return (this.selectedDate.value as Date)
  }

  filterOrders() {
    this.orders?.sort((a, b) => {
      if (b.createdAt == null) {
        return 1;
      } else if (a.createdAt == null) {
        return -1
      } else {
        return new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime()
      }
    })
    this.filtered = this.orders
  }

  onFilterChange() {
    this.filtered = this.orders

    if (!this.displayCompleted) {
      this.filtered = this.filtered?.filter(value => {
        return value.status != StatusEnum.COMPLETED
      })
    }

    console.log("filtered: " + this.filtered)
  }

}
