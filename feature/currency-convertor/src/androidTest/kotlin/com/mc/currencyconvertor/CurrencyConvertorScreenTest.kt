package com.mc.currencyconvertor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class CurrencyConvertorScreenTest {

    @get: Rule
    val composeTestRule = createComposeRule()

    @Test
    fun initialStateAssertLoadingVisible() {
        composeTestRule.setContent {
            CurrencyConvertorScreen(
                uiState = CurrencyConvertorUiState(),
                onFromCurrencyChange = {},
                onToCurrencyChange = {},
                onSwap = {}
            )
        }
        composeTestRule.onNodeWithTag("loading").assertExists()
    }

    @Test
    fun swapClickAssertChangingCurrencies() {
        var fromCurrency = CurrencyUiModel("USD", "1.0")
        var toCurrency = CurrencyUiModel("EUR", "0.92")

        composeTestRule.setContent {
            var uiState by remember {
                mutableStateOf(
                    CurrencyConvertorUiState(
                        fromCurrency = fromCurrency,
                        toCurrency = toCurrency
                    )
                )
            }
            CurrencyConvertorScreen(
                uiState = uiState,
                onFromCurrencyChange = {},
                onToCurrencyChange = {},
                onSwap = {
                    val temp = fromCurrency
                    fromCurrency = toCurrency
                    toCurrency = temp

                    uiState = uiState.copy(
                        fromCurrency = fromCurrency,
                        toCurrency = toCurrency
                    )
                }
            )
        }

        composeTestRule.onNodeWithText("USD").assertExists()
        composeTestRule.onNodeWithText("EUR").assertExists()

        assert(fromCurrency.code == "USD")
        assert(toCurrency.code == "EUR")

        composeTestRule.onNodeWithTag("swap").performClick()

        assert(fromCurrency.code == "EUR")
        assert(toCurrency.code == "USD")
    }

}