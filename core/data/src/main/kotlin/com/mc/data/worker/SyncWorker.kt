package com.mc.data.worker

import Synchronizer
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.mc.data.repository.CurrencyRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val currencyRepository: CurrencyRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            currencyRepository.syncWith(Synchronizer())
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        const val NAME = "SyncWorker"

        val request = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(WorkerUtil.DefaultConstraints)
            .build()
    }

}













