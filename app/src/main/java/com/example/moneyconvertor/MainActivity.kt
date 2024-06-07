package com.example.moneyconvertor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mc.currencyconvertor.CurrencyConvertorRoute
import com.mc.currencyconvertor.CurrencyConvertorScreen
import com.mc.designsystem.theme.MoneyConvertorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MoneyConvertorTheme {
                CurrencyConvertorRoute()
            }
        }
    }
}