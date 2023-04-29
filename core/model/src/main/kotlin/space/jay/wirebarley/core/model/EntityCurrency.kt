package space.jay.wirebarley.core.model

data class EntityCurrency(
    val timeMillis : Long? = null,
    val source : TypeCountryAndQuote? = null,
    val quotes : Map<TypeCountryAndQuote, Double> = emptyMap()
)

const val QUOTE_NON_VALUE = 0.0
