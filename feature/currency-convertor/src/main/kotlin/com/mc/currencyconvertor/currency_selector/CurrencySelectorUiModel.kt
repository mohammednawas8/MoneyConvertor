package com.mc.currencyconvertor.currency_selector

import com.mc.model.currency_convertor.CurrencyType

data class CurrencySelectorUiModel(
    val selectedCurrencyIndex: Int,
    val currencies: List<String>,
    val searchQuery: String,
    val type: CurrencyType
) {
    companion object {
        val preview = CurrencySelectorUiModel(
            selectedCurrencyIndex = 1,
            currencies = listOf("USD","EUR","ILS","AED","LIR"),
            searchQuery = "",
            type = CurrencyType.FROM
        )
    }
}