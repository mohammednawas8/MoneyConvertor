package com.mc.currencyconvertor.currency_selector

data class CurrencySelectorUiModel(
    val selectedCurrencyIndex: Int,
    val currencies: List<String>
) {
    companion object {
        val preview = CurrencySelectorUiModel(
            selectedCurrencyIndex = 1,
            currencies = listOf("USD","EUR","ILS","AED","LIR")
        )
    }
}