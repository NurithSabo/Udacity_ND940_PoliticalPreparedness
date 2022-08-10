package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

//TODO: Create Factory to generate ElectionViewModel with provided election datasource
class ElectionsViewModelFactory (
    private val repository : ElectionsRepository ): ViewModelProvider.Factory
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ElectionsViewModel::class.java))
        {
            return ElectionsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unable to construct")
    }
}