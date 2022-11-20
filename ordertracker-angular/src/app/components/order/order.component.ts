import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import * as Order  from "../../api/ordertracker-backend/model/order";
import {OrderControllerService} from "../../api/ordertracker-backend";

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss']
})
export class OrderComponent implements OnInit {

  constructor(private _orderService: OrderControllerService) { }

  @Input() order: Order.Order = {}

  statuses = Order.Order.Statuses

  isUpdating = false

  @Output() updateEvent = new EventEmitter<string>();

  ngOnInit(): void {
  }

  updateStatus() {
    this.isUpdating = true
    this._orderService.updateOrderStatus( { orderId: this.order.id, newStatus: this.order.status }).subscribe(response => {
      this.isUpdating = false
      this.updateEvent.emit("")
    })
  }

}
