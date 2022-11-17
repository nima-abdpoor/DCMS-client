package com.nima.network.manager.wrapper

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.UnknownHostException

//
//suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ResultWrapper<T> {
//    return withContext(Dispatchers.IO) {
//        try {
//            processResponse(apiCall.invoke())
//        } catch (e: Exception) {
//            when (e) {
//                is UnknownHostException, is NoInternetException -> ResultWrapper.NetworkError(
//                    getContext().getString(R.string.no_network_available)
//                )
//                is IOException -> ResultWrapper.NetworkError(getContext().getString(R.string.retrofit_server_err))
//                else -> ResultWrapper.GenericError(
//                    error = ErrorResponse(
//                        message = getContext().getString(
//                            R.string.retrofit_server_not_found
//                        )
//                    )
//                )
//            }
//        }
//    }
//}


//private fun <T> processResponse(response: Response<T>): ResultWrapper<T> {
//    if (response.isSuccessful) return ResultWrapper.Success(response.getBody())
//    else
//        response.errorBody()?.let {
//            return ResultWrapper.GenericError(
//                error = convertErrorBody(
//                    it.string(),
//                    response
//                )
//            )
//        }
//    return ResultWrapper.GenericError(error = ErrorResponse(message = "خطای ناشناخته !"))
//}



//private fun getErrorMessageRelatedStatusCode(response: Response<*>): String? {
//    val errorHandlerCoordinator = ErrorHandlerCoordinatorFactory.create()
//    return errorHandlerCoordinator.handleError(response.code())
//}