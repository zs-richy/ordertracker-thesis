package hu.unideb.inf.ordertrackerandroid.network

import hu.unideb.inf.ordertrackerandroid.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception

open class CustomSuspendCall<T : Any, R : BaseResponse>(
    val genericRequest: T,
    val genericResponse: Class<R>,
    val suspendCall: suspend (T) -> (Response<R>)
) {

    suspend fun callFunction(): R {
        try {
            var response: R
            withContext(Dispatchers.IO) {
                val apiResponse = suspendCall.invoke(genericRequest)
                if (apiResponse.isSuccessful) {
                    response = apiResponse.body() ?: genericResponse.newInstance()
                    response.succeeded = true
                } else {
                    response = NetworkUtils.parseErrorBody(
                        apiResponse.errorBody().toString(),
                        genericResponse
                    )
                }
            }
            return response
        } catch (e: Exception) {
            e.printStackTrace()
            return genericResponse.newInstance()
        }
    }

}