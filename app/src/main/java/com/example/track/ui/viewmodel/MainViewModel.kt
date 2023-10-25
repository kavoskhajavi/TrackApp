package com.example.track.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.track.db.Run
import com.example.track.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor( private val mainRepository: MainRepository)
    :ViewModel() {


     val runSortedByDate = mainRepository.getAllRunsSortedByDate()

        fun insertRun(run: Run) = viewModelScope.launch {
            mainRepository.insertRun(run)
        }

}