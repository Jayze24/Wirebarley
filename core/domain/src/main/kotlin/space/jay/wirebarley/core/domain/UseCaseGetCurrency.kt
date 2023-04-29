package space.jay.wirebarley.core.domain

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import space.jay.wirebarley.core.common.di.DispatcherIO
import space.jay.wirebarley.core.data.RepositoryCurrency
import space.jay.wirebarley.core.model.TypeCountryAndQuote
import javax.inject.Inject

@ViewModelScoped
class UseCaseGetCurrency @Inject constructor(
    @DispatcherIO private val dispatcher : CoroutineDispatcher,
    private val repository : RepositoryCurrency
) {

    suspend operator fun invoke(from : TypeCountryAndQuote, to : List<TypeCountryAndQuote>) = withContext(dispatcher) {
        return@withContext repository.getCurrency(from, to)
    }

}