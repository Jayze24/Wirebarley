package space.jay.wirebarley.core.data

import space.jay.wirebarley.core.common.wapper.Result
import space.jay.wirebarley.core.model.EntityCurrency
import space.jay.wirebarley.core.model.TypeCountryAndQuote

interface RepositoryCurrency {

    suspend fun getCurrency(from : TypeCountryAndQuote, to : List<TypeCountryAndQuote>) : Result<EntityCurrency>

}