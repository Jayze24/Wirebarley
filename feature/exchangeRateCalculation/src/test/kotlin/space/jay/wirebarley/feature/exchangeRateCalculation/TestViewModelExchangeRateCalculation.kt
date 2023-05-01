package space.jay.wirebarley.feature.exchangeRateCalculation

import android.content.Context
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import space.jay.wirebarley.core.domain.UseCaseGetCurrency
import space.jay.wirebarley.core.domain.UseCaseGetExchangeRateCalculation
import space.jay.wirebarley.core.model.TypeCountryAndQuote
import space.jay.wirebarley.feature.exchangeRateCalculation.fake.FakeRepositoryCurrency
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class TestViewModelExchangeRateCalculation {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @Inject @ApplicationContext lateinit var context : Context
    private val dispatcher : TestDispatcher = UnconfinedTestDispatcher()
    lateinit var viewModel : ViewModelExchangeRateCalculation

    @Before
    fun setUp() {
        hiltRule.inject()
        val useCaseGetCurrency = UseCaseGetCurrency(dispatcher, FakeRepositoryCurrency())
        val useCaseGetExchangeRateCalculation = UseCaseGetExchangeRateCalculation(context)

        viewModel = ViewModelExchangeRateCalculation(
            context = context,
            useCaseGetCurrency = useCaseGetCurrency,
            useCaseGetExchangeRateCalculation = useCaseGetExchangeRateCalculation
        )
        viewModel.setExchangeAmount("10")
    }

    @Test
    fun viewModelExchangeRateCalculation_getCurrency() = runTest {
        viewModel.stateUIExchangedRateCalculation.test {
            // init 에서 getCurrency를 함
            assertThat(expectMostRecentItem()).isInstanceOf(StateUIExchangedRateCalculation.HasCurrency::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun viewModelExchangeRateCalculation_setExchangeAmount() = runTest {
        viewModel.stateUIExchangedRateCalculation.test {
            viewModel.setExchangeAmount("100")
            val item = expectMostRecentItem()
            assertThat(item).isInstanceOf(StateUIExchangedRateCalculation.HasCurrency::class.java)
            if (item is StateUIExchangedRateCalculation.HasCurrency) {
                assertThat(item.receivedAmount).isEqualTo("수취금액은 133,696.50 KRW 입니다")
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun viewModelExchangeRateCalculation_selectCountry() = runTest {
        viewModel.stateUIExchangedRateCalculation.test {
            viewModel.selectCountry(TypeCountryAndQuote.PHILIPPINES)
            val item = expectMostRecentItem()
            assertThat(item).isInstanceOf(StateUIExchangedRateCalculation.HasCurrency::class.java)
            if (item is StateUIExchangedRateCalculation.HasCurrency) {
                assertThat(item.receivedAmount).isEqualTo("수취금액은 555.91 PHP 입니다")
            }

            cancelAndIgnoreRemainingEvents()
        }
    }
}