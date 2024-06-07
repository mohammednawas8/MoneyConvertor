package com.mc.network.service

import com.mc.network.model.response.currency_convertor.ExchangeRatesResponse
import retrofit2.http.GET

interface CurrencyService {

    @GET("latest")
    suspend fun getExchangeRates(): ExchangeRatesResponse
}