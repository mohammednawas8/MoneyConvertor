package com.mc.data.di

import com.mc.data.worker.SyncManager
import com.mc.data.worker.WorkManagerSyncManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ManagerModule {

    @Binds
    abstract fun bindSyncManager(impl: WorkManagerSyncManager): SyncManager

}