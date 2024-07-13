package com.mc.data.repository

import Syncable
import com.mc.model.currency_convertor.ExchangeRates
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository: Syncable {

    fun getExchangeRates(): Flow<ExchangeRates>

}