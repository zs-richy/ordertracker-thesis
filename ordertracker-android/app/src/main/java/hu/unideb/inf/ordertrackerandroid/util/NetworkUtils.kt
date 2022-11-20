package hu.unideb.inf.ordertrackerandroid.util

import com.google.gson.Gson
import java.lang.Exception

object NetworkUtils {

    fun <T: Any> parseErrorBody(bodyAsString: String?, classType: Class<T>): T {
        try {
            return Gson().fromJson(bodyAsString, classType)
        } catch (e: Exception) {
            return classType.newInstance()
        }
    }
}