package com.example.astroid.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface Dao {

    @Query("DELETE FROM asteroid WHERE close_approach_date < date('now')")
    fun deletePreviousDayAsteroids()

    @Query("SELECT * FROM asteroid ORDER BY close_approach_date ASC")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>


    @Query("SELECT * FROM asteroid WHERE close_approach_date = Date('now')")
    fun getAsteroidsToday(): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroid WHERE close_approach_date != Date('now') ORDER BY close_approach_date")
    fun getAsteroidsWeek(): LiveData<List<DatabaseAsteroid>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroids: Array<DatabaseAsteroid>)


}