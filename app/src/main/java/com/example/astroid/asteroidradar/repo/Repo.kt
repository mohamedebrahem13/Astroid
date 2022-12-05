package com.example.astroid.asteroidradar.repo

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.astroid.asteroidradar.domain.Asteroid
import com.example.astroid.asteroidradar.Constants
import com.example.astroid.asteroidradar.api.Retrofit
import com.example.astroid.asteroidradar.api.asDatabaseModel
import com.example.astroid.asteroidradar.api.asDomainModel
import com.example.astroid.asteroidradar.api.parseAsteroidsJsonResult
import com.example.astroid.asteroidradar.database.RoomDataBase
import com.example.astroid.asteroidradar.database.asDomainModel
import com.example.astroid.asteroidradar.domain.PictureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

enum class NetworkStatus { ERROR,LOADING, DONE }

class Rpo (private val database:RoomDataBase){
//get asteroids from database and convert from database object to domain model it is a separation of concern
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.dao.getAllAsteroids()){
            it.asDomainModel()
        }
// get asteroid by day from database
    val asteroidsToday: LiveData<List<Asteroid>> =
        Transformations.map(database.dao.getAsteroidsToday()) {
            it.asDomainModel()
        }
    // get asteroid by week from database
    val asteroidsWeek: LiveData<List<Asteroid>> =
        Transformations.map(database.dao.getAsteroidsWeek()) {
            it.asDomainModel()
        }
// chek network for asteroids call
    private val _networkstate = MutableLiveData<NetworkStatus>()
    val networkstate: LiveData<NetworkStatus>
        get() = _networkstate

//chek network for pic of the day call
    private val _picStatus = MutableLiveData<NetworkStatus>()
    val picStatus: LiveData<NetworkStatus>
        get() = _picStatus


// get pic of the day and set it in live data
    private val _pictureOfTheDay = MutableLiveData<PictureOfDay?>()
    val pictureOfTheTheDay: LiveData<PictureOfDay?>
        get() = _pictureOfTheDay

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun  refreshAsteroids(){

        withContext(Dispatchers.IO) {
            _networkstate.postValue(NetworkStatus.LOADING)

            try {

                val asteroidList = Retrofit.api?.getAsteroidListAsync(today(),
                    seventhDay(),
                    Constants.API_KEY)
                    ?.await()

                if (asteroidList != null) {
                    Log.e("asteroids", asteroidList)
                }

                val asteroidParsed = parseAsteroidsJsonResult(JSONObject(asteroidList.toString()))

                // add asteroid to data base as database object
                database.dao.insertAll(asteroidParsed.asDatabaseModel())
                Log.d("inset steroids ", "Success")
                _networkstate.postValue(NetworkStatus.DONE)



            } catch (e: Exception) {
                e.localizedMessage?.let { Log.e("refreshAsteroid", it) }
                _networkstate.postValue(NetworkStatus.ERROR)

            }
        }




    }
// delete old asteroids from data base
    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun deleteOldAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                database.dao.deletePreviousDayAsteroids()
            } catch (e: Exception) {
                Log.e(" delete", e.message.toString())
            }
        }
    }

    // call api to get pic of the day
    suspend fun refreshPictureOfTheDay() {
        withContext(Dispatchers.IO) {
            _picStatus.postValue(NetworkStatus.LOADING)
            try {
                _pictureOfTheDay.postValue(
                    Retrofit.api?.getPictureOfTheDay(Constants.API_KEY)?.asDomainModel()
                )
                _picStatus.postValue(NetworkStatus.DONE)
            } catch (e: Exception) {
                Log.e("Asteroid Radar", "refreshPictureOfTheDay(): " + e.message.toString())
                _pictureOfTheDay.postValue(null)
                _picStatus.postValue(NetworkStatus.ERROR)
            }
        }
    }

}


@SuppressLint("WeekBasedYear")
@RequiresApi(Build.VERSION_CODES.N)
private fun formatDate(date: Date): String {
    val dateFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    } else {
        TODO("VERSION.SDK_INT < N")
    }
    return dateFormat.format(date)
}

@RequiresApi(Build.VERSION_CODES.N)
fun today(): String {
    val calendar = Calendar.getInstance()
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        formatDate(calendar.time)
    } else {
        TODO("VERSION.SDK_INT < N")
    }
}

// get day 7 with format date
@RequiresApi(Build.VERSION_CODES.N)
fun seventhDay(): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 7)
    return formatDate(calendar.time)
}