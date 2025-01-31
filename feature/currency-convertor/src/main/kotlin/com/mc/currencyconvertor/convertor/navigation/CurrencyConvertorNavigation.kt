package com.mc.currencyconvertor.convertor.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mc.currencyconvertor.convertor.CurrencyConvertorRoute
import com.mc.currencyconvertor.convertor.NavigateToCurrenciesSelector
import com.mc.model.currency_convertor.CurrencyType
import kotlinx.serialization.Serializable

@Serializable
data object CurrencyConvertorRoute

fun NavGraphBuilder.currencyConvertorScreen(
    navigateToCurrenciesSelector: NavigateToCurrenciesSelector
) {
    composable<CurrencyConvertorRoute> {
        CurrencyConvertorRoute(navigateToCurrenciesSelector = navigateToCurrenciesSelector)
    }
}