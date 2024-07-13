package com.mc.currencyconvertor

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mc.data.repository.CurrencyRepository
import com.mc.data.worker.SyncManager
import com.mc.data.worker.WorkManagerSyncManager
import com.mc.model.currency_convertor.CurrencyInfo
import com.mc.model.currency_convertor.ExchangeRates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CurrencyConvertorViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val sharedPreferences: SharedPreferences,
    workManagerSyncManager: SyncManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CurrencyConvertorUiState())
    val uiState = _uiState.asStateFlow()

    private lateinit var exchangeRates: ExchangeRates

    companion object {
        const val fromCurrencyKey = "fromCurrencyKey"
        const val toCurrencyKey = "toCurrencyKey"
    }

    init {
        workManagerSyncManager
            .isSyncing
            .onEach { isLoading ->
                _uiState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }.launchIn(viewModelScope)
        initUiState()
    }

    private fun initUiState() {
        currencyRepository
            .getExchangeRates()
            .retryWhen { cause, _ -> cause is IllegalStateException }
            .onEach { exchangeRates ->
                if (exchangeRates.rates.isEmpty()) {
                    return@onEach
                }
                this.exchangeRates = exchangeRates
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        allCurrencies = exchangeRates.rates.keys.map {
                            CurrencyUiModel(code = it, "")
                        },
                        lastUpdated = fromDate(exchangeRates.lastUpdatedDate),
                    )
                }
                setInitialCurrencies()
            }.launchIn(viewModelScope)
    }

    private fun setInitialCurrencies() {
        val initialCurrencies = getUserCurrencies()

        val convertedResult = convert(
            fromVsBaseValue = initialCurrencies.first.value,
            toVsBaseValue = initialCurrencies.second.value,
            amount = 1.0
        )
        _uiState.update {
            it.copy(
                fromCurrency = CurrencyUiModel(initialCurrencies.first.code, "1.00"),
                toCurrency = CurrencyUiModel(initialCurrencies.second.code, convertedResult),
                indicativeExchangeRate = "1 ${initialCurrencies.first.code} = $convertedResult ${initialCurrencies.second.code}"
            )
        }
    }


    private fun getUserCurrencies(): Pair<CurrencyInfo, CurrencyInfo> {
        val fromCurrencyCode = sharedPreferences.getString(fromCurrencyKey,null)
        val toCurrencyCode = sharedPreferences.getString(toCurrencyKey,null)
        var fromCurrency = CurrencyInfo("USD", exchangeRates.rates.getValue("USD"))
        var toCurrency = CurrencyInfo("EUR", exchangeRates.rates.getValue("EUR"))

        if (fromCurrencyCode != null) {
            fromCurrency = CurrencyInfo(fromCurrencyCode, exchangeRates.rates.getValue(fromCurrencyCode))
        }

        if (toCurrencyCode != null) {
            toCurrency = CurrencyInfo(toCurrencyCode, exchangeRates.rates.getValue(toCurrencyCode))
        }

        return Pair(fromCurrency, toCurrency)
    }


    fun onFromCurrencyChange(fromCurrency: CurrencyUiModel) {
        when (val validationResult = fromCurrency.value.safeToDouble()) {
            is StringToDoubleConversionResult.Valid -> {
                with(uiState.value) {
                    val convertResult = convert(
                        fromVsBaseValue = exchangeRates.rates.getValue(fromCurrency.code),
                        toVsBaseValue = exchangeRates.rates.getValue(toCurrency.code),
                        amount = validationResult.value
                    )

                    _uiState.update {
                        it.copy(
                            fromCurrency = fromCurrency,
                            toCurrency = toCurrency.copy(value = convertResult),
                            indicativeExchangeRate = "1 ${fromCurrency.code} = ${
                                getIndicativeExchangeRate(
                                    fromCurrency.code,
                                    toCurrency.code
                                )
                            } ${toCurrency.code}"
                        )
                    }
                }
                sharedPreferences.edit().putString(fromCurrencyKey, fromCurrency.code).apply()
            }

            is StringToDoubleConversionResult.Empty -> {
                _uiState.update {
                    it.copy(
                        fromCurrency = it.fromCurrency.copy(value = ""),
                        toCurrency = it.toCurrency.copy(value = ""),
                    )
                }
            }

            StringToDoubleConversionResult.Invalid -> Unit
        }
    }

    fun onToCurrencyChange(toCurrency: CurrencyUiModel) {
        if (toCurrency.code != uiState.value.toCurrency.code) {
            _uiState.update {
                it.copy(
                    toCurrency = toCurrency
                )
            }
            onFromCurrencyChange(
                fromCurrency = uiState.value.fromCurrency
            )

            sharedPreferences.edit().putString(toCurrencyKey, toCurrency.code).apply()

            return
        }

        when (val validationResult = toCurrency.value.safeToDouble()) {
            is StringToDoubleConversionResult.Valid -> {
                with(uiState.value) {
                    val convertResult = convert(
                        fromVsBaseValue = exchangeRates.rates.getValue(toCurrency.code),
                        toVsBaseValue = exchangeRates.rates.getValue(fromCurrency.code),
                        amount = validationResult.value
                    )

                    _uiState.update {
                        it.copy(
                            fromCurrency = fromCurrency.copy(value = convertResult),
                            toCurrency = toCurrency
                        )
                    }
                }
            }

            is StringToDoubleConversionResult.Empty -> {
                _uiState.update {
                    it.copy(
                        fromCurrency = it.fromCurrency.copy(value = ""),
                        toCurrency = it.toCurrency.copy(value = ""),
                    )
                }
            }

            StringToDoubleConversionResult.Invalid -> Unit

        }
    }

    fun swapCurrencies() {
        _uiState.update {
            it.copy(
                fromCurrency = it.toCurrency,
                toCurrency = it.fromCurrency
            )
        }
    }

    private fun String.safeToDouble(): StringToDoubleConversionResult {
        return if (this.endsWith(".")) {
            StringToDoubleConversionResult.Valid(dropLast(1).toDouble())
        } else if (isEmpty()) {
            StringToDoubleConversionResult.Empty
        } else {
            this.toDoubleOrNull()?.let {
                StringToDoubleConversionResult.Valid(it)
            } ?: StringToDoubleConversionResult.Invalid
        }
    }

    private fun getIndicativeExchangeRate(
        fromCurrencyCode: String,
        toCurrencyCode: String
    ): String {
        return convert(
            fromVsBaseValue = exchangeRates.rates.getValue(fromCurrencyCode),
            toVsBaseValue = exchangeRates.rates.getValue(toCurrencyCode),
            amount = 1.0
        )
    }

    private fun convert(
        fromVsBaseValue: Double,
        toVsBaseValue: Double,
        amount: Double
    ): String {
        return CurrencyConvertor.convert(
            fromCurrencyRateVsBaseCurrencyRate = fromVsBaseValue,
            toCurrencyRateVsBaseCurrencyRate = toVsBaseValue,
            amount = amount
        ).let {
            String.format(Locale.ENGLISH, "%.2f", it)
        }
    }

    private fun fromDate(date: String): String {
        return try {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyy")
            OffsetDateTime.parse(date).format(formatter)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

}


private sealed interface StringToDoubleConversionResult {
    data class Valid(val value: Double) : StringToDoubleConversionResult

    data object Invalid : StringToDoubleConversionResult

    data object Empty : StringToDoubleConversionResult
}




