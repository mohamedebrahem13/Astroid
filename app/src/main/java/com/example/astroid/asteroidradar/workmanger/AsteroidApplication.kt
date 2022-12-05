package com.example.astroid.asteroidradar.workmanger

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidApplication : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun delayedInit() = applicationScope.launch {
        setupRecurringWork()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                setRequiresDeviceIdle(true)
            }.build()

        val repeatingRequest = PeriodicWorkRequestBuilder<AsteroidWork>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(AsteroidWork.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP, repeatingRequest)
    }
}