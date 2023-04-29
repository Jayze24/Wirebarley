package space.jay.wirebarley.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import space.jay.wirebarley.core.data.RepositoryCurrency
import space.jay.wirebarley.core.data.repository.RepositoryCurrencyImpl

@Module
@InstallIn(SingletonComponent::class)
interface HiltRepository {

    @Binds
    fun bindRepositoryCurrency(currency : RepositoryCurrencyImpl) : RepositoryCurrency
}