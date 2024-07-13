package com.mc.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mc.model.currency_convertor.CurrencyInfo
import com.mc.model.currency_convertor.ExchangeRates

@Entity
data class ExchangeRatesEntity(
    @PrimaryKey
    val lastUpdatedDate: String,
    val baseCurrency: String,
    val exchangeRates: Map<String, CurrencyInfo>
)

fun ExchangeRatesEntity.asExternalModel() = ExchangeRates(
    baseCurrency = baseCurrency,
    rates = exchangeRates.mapValues { it.value.value },
    lastUpdatedDate = lastUpdatedDate
)