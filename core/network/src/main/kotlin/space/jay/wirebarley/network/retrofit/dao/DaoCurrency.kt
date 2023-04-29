package space.jay.wirebarley.network.retrofit.dao

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import space.jay.wirebarley.network.model.DataCurrency

internal interface DaoCurrency {

    @GET("/currency_data/live")
    suspend fun getCurrency(@Query("source") source : String, @Query("currencies") currencies : String) : Response<DataCurrency>

}