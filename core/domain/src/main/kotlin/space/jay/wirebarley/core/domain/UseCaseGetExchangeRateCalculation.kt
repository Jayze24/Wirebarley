package space.jay.wirebarley.core.domain

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import space.jay.wirebarley.core.common.format.toPriceFormat
import space.jay.wirebarley.core.common.wapper.Result
import space.jay.wirebarley.core.common.wapper.Error
import space.jay.wirebarley.core.common.wapper.Success
import space.jay.wirebarley.core.model.QUOTE_NON_VALUE
import javax.inject.Inject

@ViewModelScoped
class UseCaseGetExchangeRateCalculation @Inject constructor(
    @ApplicationContext val context : Context
) {

    operator fun invoke(exchangeAmount : String, rate : Double?) : Result<String> {
        val amount = exchangeAmount.toDoubleOrNull()
        return when {
            amount == null -> Error(message = context.getString(R.string.please_enter_amount))
            amount !in 0.0 .. 10_000.0 -> Error(message = context.getString(R.string.amount_not_correct))
            // todo jay 에러 코드 const에로 만들어 변경
            rate == null || rate == QUOTE_NON_VALUE -> Error(code = 11_001, message = context.getString(R.string.not_contain_exchange_rate))
            else -> Success((rate * amount).toPriceFormat())
        }
    }
}