package com.mc.currencyconvertor.currency_selector

import com.mc.model.currency_convertor.CurrencyType

sealed interface CurrencySelectorEvents {
    data class SelectCurrency(val type: CurrencyType, val currency: String) : CurrencySelectorEvents
}