package space.jay.wirebarley.core.data.repository

import space.jay.wirebarley.core.common.wapper.Result
import space.jay.wirebarley.core.data.RepositoryCurrency
import space.jay.wirebarley.core.model.EntityCurrency
import space.jay.wirebarley.core.model.TypeCountryAndQuote
import space.jay.wirebarley.network.SourceCurrency
import javax.inject.Inject

class RepositoryCurrencyImpl @Inject constructor(
    private val sourceCurrency : SourceCurrency,
) : RepositoryCurrency {

    override suspend fun getCurrency(from : TypeCountryAndQuote, to : List<TypeCountryAndQuote>) : Result<EntityCurrency> {
        return sourceCurrency.getCurrency(from, to)
    }

}