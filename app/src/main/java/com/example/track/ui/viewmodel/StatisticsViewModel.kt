package com.example.track.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.track.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    val mainRepository: MainRepository) :ViewModel() {


}