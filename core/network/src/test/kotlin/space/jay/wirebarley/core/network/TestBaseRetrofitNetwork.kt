package space.jay.wirebarley.core.network

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Response
import space.jay.wirebarley.core.common.wapper.ClientError
import space.jay.wirebarley.core.common.wapper.ServerError
import space.jay.wirebarley.core.common.wapper.Success
import space.jay.wirebarley.core.common.wapper.Error
import space.jay.wirebarley.network.retrofit.BaseRetrofitNetwork
import java.net.HttpURLConnection

@RunWith(RobolectricTestRunner::class)
class TestBaseRetrofitNetwork : BaseRetrofitNetwork()  {

    @Test
    fun callApi_success_noBody() = runBlocking {
        val fakeBody : String? = null
        val result = callApi(
            api = { Response.success(HttpURLConnection.HTTP_OK, fakeBody) },
            mapping = { it.body() }
        )
        assertThat(result).isInstanceOf(Success::class.java)
        if (result is Success) {
            assertThat(result.data).isNull()
        }
    }

    @Test
    fun callApi_success_mapping() = runBlocking {
        val fakeBody = "123"
        val result = callApi(
            api = { Response.success(HttpURLConnection.HTTP_OK, fakeBody) },
            mapping = { it.body()?.toInt() }
        )
        assertThat(result).isInstanceOf(Success::class.java)
        if (result is Success) {
            assertThat(result.data).isEqualTo(fakeBody.toInt())
        }
    }

    @Test
    fun callApi_client_error() = runBlocking {
        val result = callApi(
            api = { Response.error<String>(HttpURLConnection.HTTP_BAD_REQUEST, "HTTP_BAD_REQUEST".toResponseBody()) },
            mapping = { it.body() }
        )
        assertThat(result).isInstanceOf(ClientError::class.java)
        if (result is ClientError) {
            assertThat(result.code).isIn(400 until 500)
        }
    }

    @Test
    fun callApi_server_error() = runBlocking {
        val result = callApi(
            api = { Response.error<String>(HttpURLConnection.HTTP_BAD_GATEWAY, "HTTP_BAD_GATEWAY".toResponseBody()) },
            mapping = { it.body() }
        )
        assertThat(result).isInstanceOf(ServerError::class.java)
        if (result is ServerError) {
            assertThat(result.code).isIn(500 until 600)
        }
    }

    @Test
    fun callApi_error_else() = runBlocking {
        val fakeErrorCode = 600
        val result = callApi(
            api = { Response.error<String>(fakeErrorCode, "".toResponseBody()) },
            mapping = { it.body() }
        )
        assertThat(result).isInstanceOf(Error::class.java)
        if (result is Error) {
            assertThat(result.code).isEqualTo(fakeErrorCode)
        }
    }

    @Test
    fun callApi_fail() = runBlocking {
        val fakeException = Exception("error")
        val result = callApi<String, String>(
            api = { throw fakeException },
            mapping = { it.body() ?: "" }
        )
        assertThat(result).isInstanceOf(Error::class.java)
        if (result is Error) {
            assertThat(result.message).isEqualTo(fakeException.message)
        }
    }
}