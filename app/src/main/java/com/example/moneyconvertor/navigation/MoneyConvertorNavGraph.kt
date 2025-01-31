package com.example.moneyconvertor.navigation

import androidx.compose.material.navigation.ModalBottomSheetLayout
import androidx.compose.material.navigation.rememberBottomSheetNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.mc.currencyconvertor.convertor.navigation.CurrencyConvertorRoute
import com.mc.currencyconvertor.convertor.navigation.currencyConvertorScreen
import com.mc.currencyconvertor.currency_selector.navigation.currencySelectorSheet
import com.mc.currencyconvertor.currency_selector.navigation.navigateToCurrencySelector


@Composable
fun MoneyConvertorNavGraph() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)

    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        scrimColor = Color.Transparent
    ) {
        NavHost(navController = navController, startDestination = CurrencyConvertorRoute) {
            currencyConvertorScreen(navigateToCurrenciesSelector = navController::navigateToCurrencySelector)

            currencySelectorSheet(navigateBack = navController::navigateUp)
        }
    }

}