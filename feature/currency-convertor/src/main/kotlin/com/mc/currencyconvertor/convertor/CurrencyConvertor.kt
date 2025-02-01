package com.mc.currencyconvertor.convertor

object CurrencyConvertor {

    fun convert(
        fromCurrencyRateVsBaseCurrencyRate: Double,
        toCurrencyRateVsBaseCurrencyRate: Double,
        amount: Double
    ): Double {
        return (toCurrencyRateVsBaseCurrencyRate / fromCurrencyRateVsBaseCurrencyRate) * amount
    }

}