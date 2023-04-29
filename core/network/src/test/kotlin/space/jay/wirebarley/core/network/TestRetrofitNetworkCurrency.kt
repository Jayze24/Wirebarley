package space.jay.wirebarley.core.network

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import space.jay.wirebarley.core.common.format.toDataFormat
import space.jay.wirebarley.core.common.wapper.Success
import space.jay.wirebarley.core.model.TypeCountryAndQuote
import space.jay.wirebarley.network.retrofit.RetrofitNetworkCurrency
import space.jay.wirebarley.network.retrofit.dao.DaoCurrency
import java.net.HttpURLConnection
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
class TestRetrofitNetworkCurrency {

    private lateinit var server : MockWebServer
    private lateinit var network : RetrofitNetworkCurrency

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        val dao = Retrofit.Builder()
            .baseUrl("http://localhost:${server.port}")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DaoCurrency::class.java)
        network = RetrofitNetworkCurrency(dao)
    }

    @After
    fun close() {
        server.shutdown()
    }

    @Test
    fun getCurrency_success_data_convert_to_entity() = runBlocking {
        val fakeTimestamp = 1682477884
        val fakeSource = "USD"
        val fakeKRW = 1336.965023
        val fakeJPY = 133.584497
        val fakePHP = 55.5915
        val fakeBody = """
                    {
                        "success": true,
                        "timestamp": $fakeTimestamp,
                        "source": "$fakeSource",
                        "quotes": {
                            "USDKRW": $fakeKRW,
                            "USDJPY": $fakeJPY,
                            "USDPHP": $fakePHP
                        }
                    }
                """.trimIndent()
        server.enqueue(
            MockResponse().apply {
                setResponseCode(HttpURLConnection.HTTP_OK)
                addHeader("Content-Type", "application/json")
                setBody(fakeBody)
            }
        )

        val result = network.getCurrency(TypeCountryAndQuote.USA, listOf(TypeCountryAndQuote.KOREA, TypeCountryAndQuote.JAPAN, TypeCountryAndQuote.PHILIPPINES))
        // 성공 wrapper 왔는지 확인
        assertThat(result).isInstanceOf(Success::class.java)
        if (result is Success) {
            // 데이터가 entity로 변경되었는지 확인
            val data = result.data
            assertThat(data.source?.quote).isEqualTo(fakeSource)
            assertThat(data.quotes[TypeCountryAndQuote.KOREA]).isEqualTo(fakeKRW)
            assertThat(data.quotes[TypeCountryAndQuote.JAPAN]).isEqualTo(fakeJPY)
            assertThat(data.quotes[TypeCountryAndQuote.PHILIPPINES]).isEqualTo(fakePHP)
            assertThat(data.timeMillis?.toDataFormat()).isEqualTo("2023-04-26 11:58")
        }
    }

    @Test
    fun getCurrency_success_empty_body() = runBlocking {
        server.enqueue(
            MockResponse().apply {
                setResponseCode(HttpURLConnection.HTTP_NO_CONTENT)
                addHeader("Content-Type", "application/json")
            }
        )

        val result = network.getCurrency(TypeCountryAndQuote.USA, listOf(TypeCountryAndQuote.KOREA, TypeCountryAndQuote.JAPAN, TypeCountryAndQuote.PHILIPPINES))
        // 성공 wrapper 왔는지 확인
        assertThat(result).isInstanceOf(Success::class.java)
        if (result is Success) {
            // 데이터가 entity로 변경되었는지 확인
            val data = result.data
            assertThat(data.timeMillis).isNull()
            assertThat(data.source).isNull()
            assertThat(data.quotes).isEmpty()
        }
    }

    @Test
    fun getCurrency_success_wrong_information_source() = runBlocking {
        val fakeBody = """
                    {
                        "success": true,
                        "timestamp": 1682477884,
                        "source":  "KRW",
                        "quotes": {
                            "KRWUSD": 1336.965023
                        }
                    }
                """.trimIndent()
        server.enqueue(
            MockResponse().apply {
                setResponseCode(HttpURLConnection.HTTP_OK)
                addHeader("Content-Type", "application/json")
                setBody(fakeBody)
            }
        )

        val result = network.getCurrency(TypeCountryAndQuote.USA, listOf(TypeCountryAndQuote.KOREA))
        // 성공 wrapper 왔는지 확인
        assertThat(result).isInstanceOf(Success::class.java)
        if (result is Success) {
            // 데이터가 entity로 변경되었는지 확인
            val data = result.data
            assertThat(data.timeMillis).isNull()
            assertThat(data.source).isNull()
            assertThat(data.quotes).isEmpty()
        }
    }

    @Test
    fun getCurrency_success_wrong_information_success_false() = runBlocking {
        val fakeBody = """
                    {
                        "success": false,
                        "timestamp": 1682477884,
                        "source":  "USD",
                        "quotes": {
                            "USDKRW": 1336.965023
                        }
                    }
                """.trimIndent()
        server.enqueue(
            MockResponse().apply {
                setResponseCode(HttpURLConnection.HTTP_OK)
                addHeader("Content-Type", "application/json")
                setBody(fakeBody)
            }
        )

        val result = network.getCurrency(TypeCountryAndQuote.USA, listOf(TypeCountryAndQuote.KOREA))
        // 성공 wrapper 왔는지 확인
        assertThat(result).isInstanceOf(Success::class.java)
        if (result is Success) {
            // 데이터가 entity로 변경되었는지 확인
            val data = result.data
            assertThat(data.timeMillis).isNull()
            assertThat(data.source).isNull()
            assertThat(data.quotes).isEmpty()
        }
    }
}