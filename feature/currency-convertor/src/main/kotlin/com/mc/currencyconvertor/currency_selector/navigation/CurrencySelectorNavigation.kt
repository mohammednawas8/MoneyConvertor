package com.mc.currencyconvertor.currency_selector.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.mc.currencyconvertor.convertor.CurrencyConvertorViewModel
import com.mc.currencyconvertor.currency_selector.CurrencySelectorRoute
import com.mc.currencyconvertor.currency_selector.CurrencySelectorViewModel
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
    navigateBack: () -> Unit,
    sharedViewModel: @Composable (NavBackStackEntry) -> CurrencyConvertorViewModel,
    onFromCurrencySelected: (CurrencyConvertorViewModel, String) -> Unit,
    onToCurrencySelected: (CurrencyConvertorViewModel, String) -> Unit,
    ) {
    modalBottomSheet<CurrencySelectorRoute>(
        close = { navigateBack() }
    ) { navBackStackEntry, state ->
        val scope = rememberCoroutineScope()
        val vm = sharedViewModel(navBackStackEntry)

        CurrencySelectorRoute(
            navigateBack = {
                scope.launch {
                    state.hide()
                    navigateBack()
                }
            },
            onFromCurrencySelected = { onFromCurrencySelected(vm, it) },
            onToCurrencySelected = { onToCurrencySelected(vm, it) },
        )
    }
}