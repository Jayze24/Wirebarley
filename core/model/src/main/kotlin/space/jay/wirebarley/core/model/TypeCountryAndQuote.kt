package space.jay.wirebarley.core.model

import android.content.Context

/**
 * @explanation
 * 화폐 추가시 여기만 해주면 됨.
 * 국가(화폐 단위)
 */
enum class TypeCountryAndQuote(val nameId: Int, val quote: String) {
    KOREA(R.string.korea,"KRW"),
    JAPAN(R.string.japan,"JPY"),
    PHILIPPINES(R.string.philippines, "PHP"),
    USA(R.string.usa, "USD");
}

fun TypeCountryAndQuote.toFullMark(context : Context) = "${context.getString(this.nameId)}(${this.quote})"