package com.example.track.ui.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.track.db.Run
import com.example.track.other.SortType
import com.example.track.other.SortType.AVG_SPEED
import com.example.track.other.SortType.CALORIES_BURNED
import com.example.track.other.SortType.DATE
import com.example.track.other.SortType.DISTANCE
import com.example.track.other.SortType.RUNNING_TIME
import com.example.track.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor( private val mainRepository: MainRepository)
    :ViewModel() {


    private val runsSortedByDate = mainRepository.getAllRunsSortedByDate()
    private val runsSortedByDistance = mainRepository.getAllRunsSortedByDistance()
    private val runsSortedByCaloriesBurned = mainRepository.getAllRunsSortedByCaloriesBurned()
    private val runsSortedByTimeInMillis = mainRepository.getAllRunsSortedByTimeInMillis()
    private val runsSortedByAvgSpeed = mainRepository.getAllRunsSortedByAvgSpeed()

    val runs = MediatorLiveData<List<Run>>()
     var sortType = SortType.DATE

    init {
        runs.addSource(runsSortedByDate){result->
            if (sortType == DATE){
                result?.let { runs.value = it }
            }
        }

        runs.addSource(runsSortedByAvgSpeed){result->
            if (sortType == AVG_SPEED){
                result?.let { runs.value = it }
            }
        }


        runs.addSource(runsSortedByDistance){result->
            if (sortType == DISTANCE){
                result?.let { runs.value = it }
            }
        }

        runs.addSource(runsSortedByCaloriesBurned){result->
            if (sortType == CALORIES_BURNED){
                result?.let { runs.value = it }
            }
        }

        runs.addSource(runsSortedByTimeInMillis){result->
            if (sortType == RUNNING_TIME){
                result?.let { runs.value = it }
            }
        }

    }

    fun sortRuns(sortType: SortType) = when(sortType){
        DATE -> runsSortedByDate.value?.let { runs.value = it }
        RUNNING_TIME -> runsSortedByTimeInMillis.value?.let { runs.value = it }
        AVG_SPEED -> runsSortedByAvgSpeed.value?.let { runs.value = it }
        DISTANCE -> runsSortedByDistance.value?.let { runs.value = it }
        CALORIES_BURNED -> runsSortedByCaloriesBurned.value?.let { runs.value = it }
    }.also {
        this.sortType = sortType
    }


        fun insertRun(run: Run) = viewModelScope.launch(Dispatchers.IO) {
            mainRepository.insertRun(run)
        }

}