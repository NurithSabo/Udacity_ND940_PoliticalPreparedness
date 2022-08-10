package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch
import kotlin.Exception
//TODO: Create live data val for saved elections
//TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
//TODO: Create functions to navigate to saved or upcoming election voter info
//TODO: Create live data val for upcoming elections
//TODO: Construct ViewModel and provide election datasource
    class ElectionsViewModel(
    private val repository: ElectionsRepository): ViewModel()
    {
        private val _dataLoading = MutableLiveData<Boolean>()
        val dataLoading: LiveData<Boolean> = _dataLoading

        private val _upcomingElections = MutableLiveData<List<Election>>()
        val upcomingElections : LiveData<List<Election>>
            get() = _upcomingElections

        private val _savedElections = MutableLiveData<List<Election>>()
        val savedElections : LiveData<List<Election>>
            get() = _savedElections

        private fun getUpcomingElectionsVM(){
            viewModelScope.launch {
                try {
                    val response = repository.getUpcomingElections()
                    _upcomingElections.value = response.elections
                    Log.i(TAG, "+"+response.elections.size.toString())

                }//try
                catch (e : Exception){
                    e.message?.let {
                        Log.i(TAG+"ERROR in ElectionsVM",it )
                    }//message
                }//catch
            }//coroutine
            _dataLoading.postValue(false)
        }//fun


        fun getSavedElections(){
            viewModelScope.launch {
                    _savedElections.value = repository.getSavedElectionsList()
            }
        }

        val savedElectionsForLoading = getSavedElections()

    init {
        _dataLoading.value = true
        Log.i(TAG, "dataloading: "+dataLoading.value.toString())
        getUpcomingElectionsVM()
        getSavedElections()

    }
    }