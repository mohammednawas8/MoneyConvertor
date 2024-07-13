package com.mc.data.worker

import androidx.work.Constraints
import androidx.work.NetworkType

object WorkerUtil {

    val DefaultConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

}