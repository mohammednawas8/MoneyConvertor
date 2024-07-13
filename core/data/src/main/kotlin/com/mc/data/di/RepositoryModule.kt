package com.mc.data.di

import com.mc.data.repository.CurrencyRepository
import com.mc.data.repository.OfflineFirstCurrencyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindCurrencyRepository(impl: OfflineFirstCurrencyRepository): CurrencyRepository

}