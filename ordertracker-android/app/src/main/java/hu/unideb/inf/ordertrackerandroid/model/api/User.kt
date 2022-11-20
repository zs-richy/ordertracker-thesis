package hu.unideb.inf.ordertrackerandroid.model.api

class User(
    var userName: String,
    var token: String,
    var userTokenExpire: Long) {

    val isTokenValid = userTokenExpire > System.currentTimeMillis()
}


