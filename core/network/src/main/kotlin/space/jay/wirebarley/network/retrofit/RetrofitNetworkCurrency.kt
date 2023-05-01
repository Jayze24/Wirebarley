package space.jay.wirebarley.network.retrofit

import space.jay.wirebarley.core.common.wapper.Result
import space.jay.wirebarley.core.common.wapper.Success
import space.jay.wirebarley.core.model.EntityCurrency
import space.jay.wirebarley.core.model.TypeCountryAndQuote
import space.jay.wirebarley.network.SourceCurrency
import space.jay.wirebarley.network.model.DataCurrency
import space.jay.wirebarley.network.model.asEntity
import space.jay.wirebarley.network.retrofit.dao.DaoCurrency
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitNetworkCurrency @Inject internal constructor(
    private val networkApi : DaoCurrency
) : BaseRetrofitNetwork(), SourceCurrency {

    override suspend fun getCurrency(from : TypeCountryAndQuote, to : List<TypeCountryAndQuote>) : Result<EntityCurrency> {
        return callApi(
            api = { networkApi.getCurrency(source = from.quote, currencies = to.joinToString(",") { it.quote }) },
            mapping = { it.body().asEntity(fromQuote = from, requestedQuote = to) }
        )
        // return Success(
        //     DataCurrency(
        //         success = true,
        //         timestamp = System.currentTimeMillis() / 1000,
        //         source = "USD",
        //         quotes = mapOf(
        //             "USDKRW" to 1336.965023,
        //             "USDJPY" to 133.584497,
        //             "USDPHP" to 55.5915,
        //         )
        //     ).asEntity(
        //         TypeCountryAndQuote.USA,
        //         listOf(
        //             TypeCountryAndQuote.KOREA,
        //             TypeCountryAndQuote.JAPAN,
        //             TypeCountryAndQuote.PHILIPPINES
        //         )
        //     )
        // )
    }

}

