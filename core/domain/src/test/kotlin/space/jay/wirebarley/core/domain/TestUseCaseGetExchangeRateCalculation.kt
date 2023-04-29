package space.jay.wirebarley.core.domain

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.qualifiers.ApplicationContext
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import space.jay.wirebarley.core.common.wapper.Success
import space.jay.wirebarley.core.common.wapper.Error
import space.jay.wirebarley.core.model.QUOTE_NON_VALUE

@RunWith(RobolectricTestRunner::class)
class TestUseCaseGetExchangeRateCalculation {

    private val context : Context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun invoke_success() {
        listOf(
            "10000" to 1336.965023,
            "446" to 133.584497,
            "0" to 55.5915
        ).forEach { fake ->
            val result = UseCaseGetExchangeRateCalculation(context).invoke(exchangeAmount = fake.first, rate = fake.second)
            assertThat(result).isInstanceOf(Success::class.java)
            if (result is Success) {
                assertThat(result.data).matches("\\d{1,3}(?:,\\d{3})*(\\.\\d{2})")
            }
        }
    }

    @Test
    fun invoke_error_amount_is_null() {
        listOf(
            "",
            "asadfjas;lj,@#$100"
        ).forEach { fake ->
            val result = UseCaseGetExchangeRateCalculation(context).invoke(exchangeAmount = fake, rate = 1.223)
            assertThat(result).isInstanceOf(Error::class.java)
            if (result is Error) {
                assertThat(result.message).isEqualTo(context.getString(R.string.please_enter_amount))
            }
        }
    }

    @Test
    fun invoke_error_amount_is_wrong() {
        listOf(
            "-1",
            "10001"
        ).forEach { fake ->
            val result = UseCaseGetExchangeRateCalculation(context).invoke(exchangeAmount = fake, rate = 1.223)
            assertThat(result).isInstanceOf(Error::class.java)
            if (result is Error) {
                assertThat(result.message).isEqualTo(context.getString(R.string.amount_not_correct))
            }
        }
    }

    @Test
    fun invoke_error_rate_is_wrong() {
        listOf(
            QUOTE_NON_VALUE,
            null
        ).forEach { fake ->
            val result = UseCaseGetExchangeRateCalculation(context).invoke(exchangeAmount = "100", rate = fake)
            assertThat(result).isInstanceOf(Error::class.java)
            if (result is Error) {
                assertThat(result.message).isEqualTo(context.getString(R.string.not_contain_exchange_rate))
                assertThat(result.code).isEqualTo(11_001) //todo jay 코드 const 로 만들어 사용
            }
        }
    }
}