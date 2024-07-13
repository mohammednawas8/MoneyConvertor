package com.example.moneyconvertor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.moneyconvertor.navigation.MoneyConvertorNavGraph
import com.mc.designsystem.theme.MoneyConvertorTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoneyConvertorTheme {
                MoneyConvertorNavGraph()
            }
        }
    }
}