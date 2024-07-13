package com.mc.database.convertor

import androidx.room.TypeConverter
import com.mc.model.currency_convertor.CurrencyInfo

class CurrencyRatesConvertor {

    //        "key:code,value/key:code,value/"

    @TypeConverter
    fun fromCurrenciesMapToString(currencies: Map<String, CurrencyInfo>): String {
        var result = ""
        currencies.forEach {
            result += "${it.key}:${it.value.code},${it.value.value}/"
        }
        return result
    }

    @TypeConverter
    fun fromCurrenciesStringToMap(string: String): Map<String, CurrencyInfo> {
        val result = mutableMapOf<String, CurrencyInfo>()
        val mapEntries = string.split("/")
        for (mapEntry in mapEntries) {
            val keyValue = mapEntry.split(":")
            val key = keyValue.firstOrNull() ?: break
            val value = keyValue.getOrNull(1)?.split(",") ?: break
            val currencyInfo = CurrencyInfo(value.first(), value.last().toDouble())
            result[key] = currencyInfo
        }
        return result
    }

}