package space.jay.wirebarley.feature.exchangeRateCalculation

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import space.jay.wirebarley.core.common.wapper.ErrorMessage
import space.jay.wirebarley.core.model.TypeCountryAndQuote
import space.jay.wirebarley.core.model.toFullMark

@RunWith(RobolectricTestRunner::class)
@FixMethodOrder(MethodSorters.DEFAULT)
class TestExchangeRateCalculation {

    @get:Rule
    val composeTestRule = createComposeRule() // use createAndroidComposeRule<YourActivity>() if you need access to an activity
    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val fromSelectedCountry = TypeCountryAndQuote.USA
    private val toSelectedCountry = TypeCountryAndQuote.KOREA
    private val loading = context.getString(R.string.loading)
    private val unknown = context.getString(R.string.unknown)
    private val inputText = "100"

    private val stateUILoading = StateUIExchangedRateCalculation.Loading(
        fromSelectedCountry = fromSelectedCountry,
        toSelectedCountry = toSelectedCountry,
        listAvailableCountry = listOf(TypeCountryAndQuote.KOREA, TypeCountryAndQuote.PHILIPPINES),
        exchangeRate = loading,
        checkTime = loading
    )
    private val stateUIHasCurrency = StateUIExchangedRateCalculation.HasCurrency(
        fromSelectedCountry = fromSelectedCountry,
        toSelectedCountry = toSelectedCountry,
        listAvailableCountry = listOf(TypeCountryAndQuote.KOREA, TypeCountryAndQuote.PHILIPPINES),
        exchangeRate = "1,130.95 ${toSelectedCountry.quote} / ${fromSelectedCountry.quote}",
        checkTime = "2020-07-06 02:36",
        exchangeAmount = "10",
        receivedAmount = String.format(context.getString(R.string.received_amount), "11,309.50", toSelectedCountry.quote),
        errorMessageExchangedAmount = ErrorMessage(id = 0L, message = context.getString(R.string.error_incorrect_remittance_amount))
    )
    private val stateUINoCurrency = StateUIExchangedRateCalculation.NoCurrency(
        fromSelectedCountry = fromSelectedCountry,
        toSelectedCountry = toSelectedCountry,
        listAvailableCountry = listOf(TypeCountryAndQuote.KOREA, TypeCountryAndQuote.PHILIPPINES),
        exchangeRate = unknown,
        checkTime = unknown,
        errorMessageCurrency = ErrorMessage(id = 0L, message = unknown)
    )

    @Test
    fun screenExchangeRateCalculation_loading() {
        composeTestRule.setContent {
            var stateUI : StateUIExchangedRateCalculation by remember { mutableStateOf(stateUINoCurrency) }
            ScreenExchangeRateCalculation(
                stateUI = stateUI,
                onChangedExchangeAmount = {},
                onChangedCountry = {},
                onGetCurrency = { stateUI = stateUILoading }
            )
        }

        // NoCurrency -> Loading 상태 변경
        composeTestRule
            .onNodeWithText(context.getString(R.string.get_latest_exchange_rates))
            .performClick()
            .assertExists()

        // 상태별 기본 화면 검사
        checkBasicScreen()

        // 환율, 조회 시간 Loading...으로 뜨는지 확인
        composeTestRule
            .onAllNodesWithText(context.getString(R.string.loading))
            .assertCountEquals(2)

        // 송금액 입력란 안뜨는지 확인
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.remittance_amount))
            .assertDoesNotExist()

        // 최신 환율 조회 버튼 확인
        composeTestRule
            .onNodeWithText(context.getString(R.string.get_latest_exchange_rates))
            .assertExists()
    }

    @Test
    fun screenExchangeRateCalculation_hasCurrency() {
        composeTestRule.setContent {
            var stateUI : StateUIExchangedRateCalculation by remember { mutableStateOf(stateUINoCurrency) }
            ScreenExchangeRateCalculation(
                stateUI = stateUI,
                onChangedExchangeAmount = {
                    assertThat(it)
                        .isAnyOf(stateUIHasCurrency.exchangeAmount, inputText+stateUIHasCurrency.exchangeAmount)
                },
                onChangedCountry = {},
                onGetCurrency = { stateUI = stateUIHasCurrency }
            )
        }

        // NoCurrency -> HasCurrency 상태 변경
        composeTestRule
            .onNodeWithText(context.getString(R.string.get_latest_exchange_rates))
            .performClick()
            .assertExists()

        // 상태별 기본 화면 검사
        checkBasicScreen()

        // 환율, 조회 시간에 있던 Loading... 문구가 없어 졌는지 확인
        composeTestRule
            .onAllNodesWithText(context.getString(R.string.loading))
            .assertCountEquals(0)

        // 환율 내용 확인
        composeTestRule
            .onNodeWithText(stateUIHasCurrency.exchangeRate)
            .assertIsDisplayed()

        // 송금액 내용 확인
        composeTestRule
            .onNodeWithText(stateUIHasCurrency.checkTime)
            .assertIsDisplayed()

        // 송금액 입력란 확인
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.remittance_amount))
            .assertExists()
        composeTestRule
            .onNodeWithText(stateUIHasCurrency.exchangeAmount)
            .assertExists()
            .performClick()
            .assertIsFocused()
            .performTextInput(inputText)
        composeTestRule
            .onNodeWithText(context.getString(R.string.error_incorrect_remittance_amount))
            .assertExists()

        // 수취금액 문구 확인
        composeTestRule
            .onNode(hasTextExactly(stateUIHasCurrency.receivedAmount))
            .assertExists()

    }

    @Test
    fun screenExchangeRateCalculation_noCurrency() {
        composeTestRule.setContent {
            var stateUI : StateUIExchangedRateCalculation by remember { mutableStateOf(stateUILoading) }
            ScreenExchangeRateCalculation(
                stateUI = stateUI,
                onChangedExchangeAmount = {},
                onChangedCountry = {},
                onGetCurrency = { stateUI = stateUINoCurrency }
            )
        }

        // Loading -> NoCurrency 상태 변경
        composeTestRule
            .onNodeWithText(context.getString(R.string.get_latest_exchange_rates))
            .performClick()
            .assertExists()

        // 상태별 기본 화면 검사
        checkBasicScreen()

        // 환율, 조회 시간 unknown 으로 뜨는지 확인
        composeTestRule
            .onAllNodesWithText(context.getString(R.string.unknown))
            .assertCountEquals(2)

        // 송금액 입력란 안뜨는지 확인
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.remittance_amount))
            .assertDoesNotExist()

        // 최신 환율 조회 버튼 확인
        composeTestRule
            .onNodeWithText(context.getString(R.string.get_latest_exchange_rates))
            .assertExists()
    }

    private fun checkBasicScreen() {
        // 송금 국가 확인
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.remittance_country))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fromSelectedCountry.toFullMark(context))
            .assertIsDisplayed()

        // 수취 국가 확인
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.recipient_country))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(toSelectedCountry.toFullMark(context))
            .assertIsDisplayed()

        // 환율 타이틀 확인
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.exchange_rate))
            .assertIsDisplayed()

        // 조회 시간 타이틀 확인
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.check_time))
            .assertIsDisplayed()
    }
}