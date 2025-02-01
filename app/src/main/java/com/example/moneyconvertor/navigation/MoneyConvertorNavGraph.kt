package com.example.moneyconvertor.navigation

import androidx.compose.material.navigation.ModalBottomSheetLayout
import androidx.compose.material.navigation.rememberBottomSheetNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.mc.currencyconvertor.convertor.CurrencyConvertorViewModel
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
            currencyConvertorScreen(
                viewModel = { navController.currencyConvertorViewModel(it) },
                navigateToCurrenciesSelector = navController::navigateToCurrencySelector,
            )

            currencySelectorSheet(
                navigateBack = navController::navigateUp,
                sharedViewModel = { navController.currencyConvertorViewModel(it) },
                onFromCurrencySelected = { vm, currency ->
                    vm.onFromCurrencyCodeChange(currency)
                },
                onToCurrencySelected = { vm, currency ->
                    vm.onToCurrencyCodeChange(currency)
                }
            )
        }
    }
}

@Composable
private fun NavController.currencyConvertorViewModel(navBackStackEntry: NavBackStackEntry): CurrencyConvertorViewModel {
    val parentEntry = remember(navBackStackEntry) {
        getBackStackEntry(CurrencyConvertorRoute)
    }

    return hiltViewModel(parentEntry)
}