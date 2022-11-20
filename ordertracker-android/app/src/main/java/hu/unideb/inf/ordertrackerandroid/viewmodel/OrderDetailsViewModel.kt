package hu.unideb.inf.ordertrackerandroid.viewmodel

import android.app.Application
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.*
import hu.unideb.inf.ordertrackerandroid.R
import hu.unideb.inf.ordertrackerandroid.model.api.Order
import hu.unideb.inf.ordertrackerandroid.network.ApiDataSource
import hu.unideb.inf.ordertrackerandroid.network.OrderStatus
import hu.unideb.inf.ordertrackerandroid.util.DateFormat
import hu.unideb.inf.ordertrackerandroid.util.toFormattedString
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OrderDetailsViewModel(application: Application): AndroidViewModel(application) {

    val dataSource = ApiDataSource()

    private var orderId: Long = -1

    private val _order = MutableLiveData<Order>()
    val order: LiveData<Order>
        get() = _order

    private val _isFetchingOrderData = MutableLiveData<Boolean>()
    val isFetchingOrderData: LiveData<Boolean>
        get() = _isFetchingOrderData

    val orderHeaderText = Transformations.map(_order) { order ->
        "Order #${order.id ?: "N/A"}"
    }

    val orderStatusImage = Transformations.map(_order) { order ->
        val drawable = when (order.status) {
            OrderStatus.CREATED -> R.drawable.ic_baseline_autorenew_24
            OrderStatus.PREPARING, OrderStatus.WORKING -> R.drawable.ic_chef
            OrderStatus.DELIVERING -> R.drawable.ic_baseline_directions_car_24
            OrderStatus.COMPLETED -> R.drawable.ic_baseline_done_24
            else -> R.drawable.ic_baseline_autorenew_24
        }

        return@map ResourcesCompat.getDrawable(application.resources, drawable, null)
    }

    val orderToVisibility = Transformations.map(_isFetchingOrderData) { fetchingOrder ->
        if (order.value == null || fetchingOrder == true) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    val progressToVisibility = Transformations.map(_isFetchingOrderData) { fetchingOrder ->
        if (order.value == null && fetchingOrder == false) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    val orderItemsText = Transformations.map(_order) { order ->
        var finalText = ""
        finalText = order.orderItems?.joinToString(separator = "\n") { orderItem ->
            "${(orderItem.count)}x ${orderItem.product?.name}"
        }.toString()

        finalText += "\n\nTotal price: ${order.sumOfItems}€"
        finalText += "\nLast update: ${order.statusLastModify?.toFormattedString(DateFormat.DOTMMddYYYYHHmmSS)}"

        return@map finalText
    }

    fun initByOrderId(orderId: Long) {
        this.orderId = orderId

        fetchOrderDetails()
    }

    fun fetchOrderDetails() {
        if (_isFetchingOrderData.value == true) return

        viewModelScope.launch {
            _isFetchingOrderData.value = true

            val orderResponse = dataSource.getOrderById(orderId)

            orderResponse.order?.let {
                _order.value = it
            }

            delay(500)

            _isFetchingOrderData.value = false
        }
    }

}