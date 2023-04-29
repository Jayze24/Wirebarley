package space.jay.wirebarley.network.retrofit

import retrofit2.Response
import space.jay.wirebarley.core.common.wapper.ClientError
import space.jay.wirebarley.core.common.wapper.Error
import space.jay.wirebarley.core.common.wapper.Result
import space.jay.wirebarley.core.common.wapper.ServerError
import space.jay.wirebarley.core.common.wapper.Success

abstract class BaseRetrofitNetwork {

    suspend fun <I, O> callApi(
        api : suspend () -> Response<I>,
        mapping : (Response<I>) -> O
    ) : Result<O> {
        return try {
            api().let { result ->
                when (result.code()) {
                    in 200 until 300 -> Success(mapping(result))
                    in 400 until 500 -> ClientError(result.code(), result.message())
                    in 500 until 600-> ServerError(result.code(), result.message())
                    else -> Error(code = result.code(), message = "Unknown Error - ${result.errorBody().toString()}")
                }
            }
        } catch (t : Throwable) {
            Error(message = t.message)
        }
    }
}