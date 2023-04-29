package space.jay.wirebarley.feature.exchangeRateCalculation

import space.jay.wirebarley.core.common.wapper.ErrorMessage
import space.jay.wirebarley.core.model.TypeCountryAndQuote

sealed interface StateUIExchangedRateCalculation {

    val fromSelectedCountry : TypeCountryAndQuote
    val toSelectedCountry : TypeCountryAndQuote
    val listAvailableCountry : List<TypeCountryAndQuote>
    val exchangeRate : String
    val checkTime : String

    data class Loading(
        override val fromSelectedCountry : TypeCountryAndQuote,
        override val toSelectedCountry : TypeCountryAndQuote,
        override val listAvailableCountry : List<TypeCountryAndQuote>,
        override val exchangeRate : String,
        override val checkTime : String,
    ) : StateUIExchangedRateCalculation

    data class NoCurrency(
        override val fromSelectedCountry : TypeCountryAndQuote,
        override val toSelectedCountry : TypeCountryAndQuote,
        override val listAvailableCountry : List<TypeCountryAndQuote>,
        override val exchangeRate : String,
        override val checkTime : String,
        val errorMessageCurrency : ErrorMessage?
    ) : StateUIExchangedRateCalculation

    data class HasCurrency(
        override val fromSelectedCountry : TypeCountryAndQuote,
        override val toSelectedCountry : TypeCountryAndQuote,
        override val listAvailableCountry : List<TypeCountryAndQuote>,
        override val exchangeRate : String,
        override val checkTime : String,
        val exchangeAmount : String,
        val receivedAmount : String,
        val errorMessageExchangedAmount : ErrorMessage?
    ) : StateUIExchangedRateCalculation
}
