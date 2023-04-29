package space.jay.wirebarley.feature.exchangeRateCalculation

import android.content.Context
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import space.jay.wirebarley.core.common.format.toDataFormat
import space.jay.wirebarley.core.common.format.toPriceFormat
import space.jay.wirebarley.core.common.wapper.ErrorMessage
import space.jay.wirebarley.core.model.EntityCurrency
import space.jay.wirebarley.core.model.TypeCountryAndQuote

data class StateViewModelExchangedRateCalculation(
    val isLoadingCurrency : Boolean = false,
    val errorMessageCurrency : ErrorMessage? = null,
    val currency : EntityCurrency? = null,

    val errorMessageExchangedAmount : ErrorMessage? = null,
    val exchangeAmount : String = "",
    val exchangedAmount : String? = null,

    val listAvailableCountry : List<TypeCountryAndQuote> = listOf(
        TypeCountryAndQuote.KOREA,
        TypeCountryAndQuote.JAPAN,
        TypeCountryAndQuote.PHILIPPINES
    ),
    val toSelectedCountry : TypeCountryAndQuote = listAvailableCountry.first(),
    val fromSelectedCountry : TypeCountryAndQuote = TypeCountryAndQuote.USA
) {

    fun toStateUi(context : Context) : StateUIExchangedRateCalculation {
        return when {
            isLoadingCurrency -> {
                val contentString = context.getString(R.string.loading)
                StateUIExchangedRateCalculation.Loading(
                    fromSelectedCountry = fromSelectedCountry,
                    toSelectedCountry = toSelectedCountry,
                    listAvailableCountry = listAvailableCountry,
                    exchangeRate = contentString,
                    checkTime = contentString
                )
            }
            isNoCurrency() -> {
                val contentString = context.getString(R.string.unknown)
                StateUIExchangedRateCalculation.NoCurrency(
                    fromSelectedCountry = fromSelectedCountry,
                    toSelectedCountry = toSelectedCountry,
                    listAvailableCountry = listAvailableCountry,
                    exchangeRate = contentString,
                    checkTime = contentString,
                    errorMessageCurrency = errorMessageCurrency
                )
            }
            else -> StateUIExchangedRateCalculation.HasCurrency(
                fromSelectedCountry = fromSelectedCountry,
                toSelectedCountry = toSelectedCountry,
                listAvailableCountry = listAvailableCountry,
                exchangeRate = "${currency!!.quotes[toSelectedCountry]!!.toPriceFormat()} ${toSelectedCountry.quote} / ${fromSelectedCountry.quote}",
                checkTime = currency.timeMillis!!.toDataFormat(),
                exchangeAmount = exchangeAmount,
                receivedAmount = getReceivedAmountResult(context),
                errorMessageExchangedAmount = errorMessageExchangedAmount
            )
        }
    }

    fun getNewCurrency(new : EntityCurrency) : EntityCurrency? {
        return if (isNewCurrency(new)) new else currency
    }

    private fun isNewCurrency(new : EntityCurrency) : Boolean {
        return (new.timeMillis ?: 0) > (currency?.timeMillis ?: 0)
    }

    private fun isNoCurrency() =
        currency == null
            || !currency.quotes.containsKey(toSelectedCountry)
            || currency.timeMillis == null

    private fun getReceivedAmountResult(context : Context) : String {
        return if (exchangedAmount.isNullOrEmpty()) {
            ""
        } else {
            String.format(
                context.getString(R.string.received_amount),
                exchangedAmount,
                toSelectedCountry.quote
            )
        }
    }
}