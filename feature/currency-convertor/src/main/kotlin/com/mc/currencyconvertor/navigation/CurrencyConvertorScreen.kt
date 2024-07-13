package com.mc.currencyconvertor.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mc.currencyconvertor.CurrencyConvertorRoute

const val CurrencyConvertorRoute = "currencyConvertorRoute"
fun NavGraphBuilder.currencyConvertorScreen() {
    composable(CurrencyConvertorRoute) {
        CurrencyConvertorRoute()
    }
}