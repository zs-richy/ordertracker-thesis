package hu.unideb.inf.ordertrackerandroid.model.api

import hu.unideb.inf.ordertrackerandroid.network.OrderStatus
import java.time.LocalDateTime

class Order {
    val id: Long? = null
    val userId: Long? = null
    val status: OrderStatus? = null
    val orderItems: Set<OrderItem>? = null
    val createdAt: LocalDateTime? = null
    val statusLastModify: LocalDateTime? = null
    val sumOfItems: Float? = null
}