package hu.unideb.inf.ordertrackerandroid.model.api

data class Product(
    val id: Long,
    val name: String,
    val price: Float,
    var images: Set<Image>,
    var count: Int
)