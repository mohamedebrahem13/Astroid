package com.example.astroid.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

    @Database(entities = [DatabaseAsteroid::class], version = 1, exportSchema = false)
    abstract class RoomDataBase : RoomDatabase() {
        abstract val dao: Dao
    }


    private lateinit var INSTANCE: RoomDataBase

    fun getDatabase(context: Context): RoomDataBase {
        synchronized(RoomDataBase::class.java) {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    RoomDataBase::class.java,
                    "asteroid").build()
            }
        }
        return INSTANCE
    }
