package space.jay.wirebarley.network.model

import space.jay.wirebarley.core.model.EntityCurrency
import space.jay.wirebarley.core.model.QUOTE_NON_VALUE
import space.jay.wirebarley.core.model.TypeCountryAndQuote

data class DataCurrency(
    val success : Boolean = false,
    val timestamp : Long = 0,
    val source : String = "",
    val quotes : Map<String, Double> = emptyMap()
)

fun DataCurrency?.asEntity(fromQuote : TypeCountryAndQuote, requestedQuote : List<TypeCountryAndQuote>) : EntityCurrency {
    return if (this == null || !success || source != fromQuote.quote) {
        EntityCurrency()
    } else {
        EntityCurrency(
            timeMillis = timestamp * 1000,
            source = fromQuote,
            quotes = requestedQuote.associateWith { (quotes["$source${it.quote}"] ?: QUOTE_NON_VALUE) }
        )
    }
}