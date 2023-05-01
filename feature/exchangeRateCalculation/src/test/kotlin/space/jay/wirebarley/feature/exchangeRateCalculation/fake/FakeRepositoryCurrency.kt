package space.jay.wirebarley.feature.exchangeRateCalculation.fake

import space.jay.wirebarley.core.common.wapper.Result
import space.jay.wirebarley.core.common.wapper.Success
import space.jay.wirebarley.core.data.RepositoryCurrency
import space.jay.wirebarley.core.model.EntityCurrency
import space.jay.wirebarley.core.model.TypeCountryAndQuote

class FakeRepositoryCurrency() : RepositoryCurrency {

    override suspend fun getCurrency(from : TypeCountryAndQuote, to : List<TypeCountryAndQuote>) : Result<EntityCurrency> {
        return Success(
            EntityCurrency(
                timeMillis = 1680325294000,
                source = from,
                quotes = mapOf(
                    TypeCountryAndQuote.KOREA to 1336.965023,
                    TypeCountryAndQuote.PHILIPPINES to 55.5915
                ),
            )
        )
    }
}