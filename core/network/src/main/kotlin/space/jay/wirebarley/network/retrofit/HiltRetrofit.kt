package space.jay.wirebarley.network.retrofit

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import space.jay.wirebarley.network.SourceCurrency
import space.jay.wirebarley.network.retrofit.dao.DaoCurrency
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface HiltRetrofit {

    @Binds
    fun RetrofitNetworkCurrency.bindCurrency() : SourceCurrency
}

@Module
@InstallIn(SingletonComponent::class)
internal object HiltRetrofitFactory {

    @Provides
    @Singleton
    internal fun providesOkHttpCallFactory() : OkHttpClient.Builder =
        OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BASIC)
                },
            )

    @Provides
    @Singleton
    internal fun providesWiki(okHttpBuilder : OkHttpClient.Builder) : DaoCurrency =
        Retrofit.Builder()
            .baseUrl("https://api.apilayer.com")
            .callFactory(
                okHttpBuilder.authenticator { _, response ->
                    response.request.newBuilder()
                        .header("apikey", "3LDMikLvor1rJLMffSTu8EPjEJ5nHPur") // todo jay properties로 옮기기 및 apikey 넣기 3LDMikLvor1rJLMffSTu8EPjEJ5nHPur
                        .build()
                }.build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DaoCurrency::class.java)

}
