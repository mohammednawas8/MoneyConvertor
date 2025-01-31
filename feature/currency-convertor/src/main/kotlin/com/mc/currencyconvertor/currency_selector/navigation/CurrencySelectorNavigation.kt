package com.mc.currencyconvertor.currency_selector.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.mc.currencyconvertor.currency_selector.CurrencySelectorRoute
import com.mc.model.currency_convertor.CurrencyType
import com.mc.ui.modalBottomSheet
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class CurrencySelectorRoute(
    val type: CurrencyType,
    val selectedCurrencyIndex: Int,
    val currencies: List<String>
)

fun NavController.navigateToCurrencySelector(
    type: CurrencyType,
    selectedCurrencyIndex: Int,
    currencies: List<String>
) {
    navigate(
        CurrencySelectorRoute(
            type = type,
            selectedCurrencyIndex = selectedCurrencyIndex,
            currencies = currencies
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.currencySelectorSheet(
    navigateBack: () -> Unit
) {
    modalBottomSheet<CurrencySelectorRoute>(
        close = { navigateBack() }
    ) { _, state ->
        val scope = rememberCoroutineScope()
        CurrencySelectorRoute(
            navigateBack = {
                scope.launch {
                    state.hide()
                    navigateBack()
                }
            },
            onCurrencySelected = {},
        )
    }
}