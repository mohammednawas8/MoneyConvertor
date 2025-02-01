package com.mc.currencyconvertor.convertor.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mc.currencyconvertor.convertor.CurrencyConvertorRoute
import com.mc.currencyconvertor.convertor.CurrencyConvertorViewModel
import com.mc.currencyconvertor.convertor.NavigateToCurrenciesSelector
import kotlinx.serialization.Serializable

@Serializable
data object CurrencyConvertorRoute

fun NavGraphBuilder.currencyConvertorScreen(
    viewModel: @Composable (NavBackStackEntry) -> CurrencyConvertorViewModel,
    navigateToCurrenciesSelector: NavigateToCurrenciesSelector
) {
    composable<CurrencyConvertorRoute> {
        CurrencyConvertorRoute(
            navigateToCurrenciesSelector = navigateToCurrenciesSelector,
            viewModel = viewModel(it)
        )
    }
}