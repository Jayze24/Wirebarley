package space.jay.wirebarley.network

import space.jay.wirebarley.core.common.wapper.Result
import space.jay.wirebarley.core.model.EntityCurrency
import space.jay.wirebarley.core.model.TypeCountryAndQuote

interface SourceCurrency {

    suspend fun getCurrency(from : TypeCountryAndQuote, to : List<TypeCountryAndQuote>) : Result<EntityCurrency>

}