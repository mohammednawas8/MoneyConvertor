package com.mc.data.repository

import com.mc.model.currency_convertor.ExchangeRates

interface CurrencyRepository {

    suspend fun getExchangeRates(): ExchangeRates

}