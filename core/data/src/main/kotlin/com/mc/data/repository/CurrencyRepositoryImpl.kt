package com.mc.data.repository

import com.mc.model.currency_convertor.ExchangeRates
import com.mc.network.model.response.currency_convertor.toExternalModel
import com.mc.network.service.CurrencyService
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val currencyService: CurrencyService
): CurrencyRepository {
    override suspend fun getExchangeRates(): ExchangeRates {
        return currencyService.getExchangeRates().toExternalModel(baseCurrency = "USD")
    }
}