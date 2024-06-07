package com.mc.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mc.network.interceptor.HeadersInterceptor
import com.mc.network.service.CurrencyService
import com.mc.network.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        return OkHttpClient
            .Builder()
            .addNetworkInterceptor(loggingInterceptor)
            .addNetworkInterceptor(HeadersInterceptor(mapOf("apiKey" to "cur_live_bN1PcVzWSogK7Bs5RzLWMtdktTIW8gQqbkLp0ieH")))
            .build()
    }

    @Singleton
    @Provides
    fun provideCurrencyRetrofitService(
        client: OkHttpClient
    ): CurrencyService {
        return Retrofit
            .Builder()
            .baseUrl(Constants.CurrencyConvertorBaseUrl)
            .client(client)
            .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
            .build()
            .create(CurrencyService::class.java)
    }

}