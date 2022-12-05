package com.example.astroid.asteroidradar.workmanger

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.astroid.asteroidradar.database.getDatabase
import com.example.astroid.asteroidradar.repo.Rpo
import retrofit2.HttpException

class AsteroidWork (appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
         val repository = Rpo(database)

        return try {
            repository.refreshAsteroids()
            repository.refreshPictureOfTheDay()
            repository.deleteOldAsteroids()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "AsteroidDataWorker"
    }
}