package com.mc.currencyconvertor.currency_selector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.mc.currencyconvertor.currency_selector.navigation.CurrencySelectorRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CurrencySelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args = savedStateHandle.toRoute<CurrencySelectorRoute>()

    private val _uiModel = MutableStateFlow(CurrencySelectorUiModel(args.selectedCurrencyIndex, args.currencies))
    val uiModel = _uiModel.asStateFlow()
}