package com.mc.model.currency_convertor

data class ExchangeRates(
    val baseCurrency: String,
    val rates: Map<String, Double>,
    val lastUpdatedDate: String
)