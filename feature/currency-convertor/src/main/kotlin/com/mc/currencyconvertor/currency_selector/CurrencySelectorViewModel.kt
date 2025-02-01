package com.mc.currencyconvertor.currency_selector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mc.currencyconvertor.currency_selector.navigation.CurrencySelectorRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencySelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args = savedStateHandle.toRoute<CurrencySelectorRoute>()

    private val _events = Channel<CurrencySelectorEvents>()
    val events = _events.consumeAsFlow()

    private val _uiModel = MutableStateFlow(
        CurrencySelectorUiModel(
            selectedCurrencyIndex = args.selectedCurrencyIndex,
            currencies = args.currencies,
            searchQuery = "",
            type = args.type
        )
    )
    val uiModel = _uiModel.asStateFlow()


    fun onSearchQueryChange(query: String) {
        _uiModel.update { it.copy(searchQuery = query) } // Update it individually to show the result in the ui as soon as possible.

        val filteredCurrencies =
            args.currencies.filter { it.contains(query.trim(), ignoreCase = true) }
        _uiModel.update { it.copy(currencies = filteredCurrencies) }
    }

    fun selectCurrency(currency: String) {
        viewModelScope.launch {
            _events.send(
                CurrencySelectorEvents.SelectCurrency(
                    type = args.type,
                    currency = currency
                )
            )
        }
    }
}