package hu.unideb.inf.ordertrackerandroid.viewmodel

import android.app.Application
import androidx.lifecycle.*
import hu.unideb.inf.ordertrackerandroid.handler.SharedPreferencesHandler
import hu.unideb.inf.ordertrackerandroid.model.api.User
import hu.unideb.inf.ordertrackerandroid.network.BackendApi

class UserViewModel(application: Application): AndroidViewModel(application) {

    val user = MutableLiveData<User?>(null)

    init {
        BackendApi.initRetrofitService()
    }


    fun updateSessionState() {
        user.value = SharedPreferencesHandler.loadAuthDetails(getApplication())
        user.value?.token?.let { token ->
            BackendApi.token = token
        }
    }

    fun clearUserData() {
        user.value = null
        SharedPreferencesHandler.clearAuthDetails(getApplication())
    }

}