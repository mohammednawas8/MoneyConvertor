package com.mc.testing.repository

import Synchronizer
import com.mc.data.repository.CurrencyRepository
import com.mc.model.currency_convertor.ExchangeRates
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class TestCurrencyRepository: CurrencyRepository {

    private val exchangeRates: MutableSharedFlow<ExchangeRates> = MutableSharedFlow(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override fun getExchangeRates(): Flow<ExchangeRates> {
        return exchangeRates
    }

    fun sendExchangeRates(exchangeRates: ExchangeRates) {
        this.exchangeRates.tryEmit(exchangeRates)
    }

    override suspend fun syncWith(synchronizer: Synchronizer) {}
}