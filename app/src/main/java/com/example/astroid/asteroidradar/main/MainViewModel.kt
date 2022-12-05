package com.example.astroid.asteroidradar.main

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.astroid.asteroidradar.database.getDatabase
import com.example.astroid.asteroidradar.domain.Asteroid
import com.example.astroid.asteroidradar.repo.Rpo
import kotlinx.coroutines.launch

enum class Filter { TODAY, WEEK, ALL }


@RequiresApi(Build.VERSION_CODES.N)
class MainViewModel(application: Application) : AndroidViewModel(application){

    // get a database reference
    private val database = getDatabase(application)
    // get a repo reference
    private val repository = Rpo(database)

    //get pic of the day from repo
    val pictureOfTheDay = repository.pictureOfTheTheDay

    // network state for asteroids call
    val networkState=repository.networkstate

    //network state for pic
    val picState=repository.picStatus


// filter asteroids from menu items with live data and enum class
    private val _filter = MutableLiveData(Filter.TODAY)
    val asteroids = Transformations.switchMap(_filter) {
        when (it) {
            Filter.WEEK -> repository.asteroidsWeek
            Filter.TODAY -> repository.asteroidsToday

            else ->  repository.asteroids

        }
    }

    fun setFilter(filter: Filter) {
        // set filter from main fragment when menu item clicked
        _filter.value = filter
    }



    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        viewModelScope.launch {
            try{
                // when view model first create call to get asteroids and pic of the day
                refreshAsteroidList()
                refreshPictureOfTheDay()

            }catch (e : Exception){
                Log.e("conection_error","error")
            }


        }
    }

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroid: MutableLiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid

    fun onAsteroidClicked(asteroid: Asteroid){
        //it  is the  clicked asteroid and i set it with live data to observe and navigate to detail fragment
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun onAsteroidDetailNavigated() {
        //clean the live data after navigate
        _navigateToSelectedAsteroid.value = null
    }

    fun refreshAsteroidList() {
        // refresh asteroid list
        viewModelScope.launch {
            repository.refreshAsteroids()
        }
    }

    fun refreshPictureOfTheDay() {
        // refresh pic of the day
        viewModelScope.launch {
            repository.refreshPictureOfTheDay()
        }
    }


}