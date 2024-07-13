package com.example.moneyconvertor.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.mc.currencyconvertor.navigation.CurrencyConvertorRoute
import com.mc.currencyconvertor.navigation.currencyConvertorScreen

@Composable
fun MoneyConvertorNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = CurrencyConvertorRoute) {
        currencyConvertorScreen()
    }
}