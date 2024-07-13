package com.mc.data.mapper

import com.mc.database.model.ExchangeRatesEntity
import com.mc.network.model.response.currency_convertor.ExchangeRatesResponse

fun ExchangeRatesResponse.toEntity(baseCurrencyCode: String): ExchangeRatesEntity {
    return ExchangeRatesEntity(
        baseCurrency = baseCurrencyCode,
        lastUpdatedDate = meta.lastUpdatedAt,
        exchangeRates = data
    )
}