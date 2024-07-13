package com.example.moneyconvertor

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.mc.data.repository.CurrencyRepository
import com.mc.data.worker.SyncWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MoneyConvertorApp: Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: MyHiltWorkerFactor

    override fun onCreate() {
        super.onCreate()

        WorkManager.getInstance(this).beginUniqueWork(
            SyncWorker.NAME,
            ExistingWorkPolicy.KEEP,
            SyncWorker.request
        ).enqueue()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

}

class MyHiltWorkerFactor @Inject constructor(
    private val currencyRepository: CurrencyRepository
): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when(workerClassName) {
            SyncWorker::class.java.name -> SyncWorker(appContext, workerParameters, currencyRepository)
            else -> null
        }
    }

}