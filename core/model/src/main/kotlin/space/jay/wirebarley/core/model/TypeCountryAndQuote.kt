package space.jay.wirebarley.core.model

/**
 * @explanation
 * 화폐 추가시 여기만 해주면 됨.
 * 국가(화폐 단위)
 */
enum class TypeCountryAndQuote(val quote: String) {
    KOREA("KRW"),
    JAPAN("JPY"),
    PHILIPPINES("PHP"),
    USA("USD")
}