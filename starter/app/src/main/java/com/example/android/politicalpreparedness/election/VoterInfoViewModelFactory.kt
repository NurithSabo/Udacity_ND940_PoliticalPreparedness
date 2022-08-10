package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import java.lang.IllegalArgumentException

//TODO: Create Factory to generate VoterInfoViewModel with provided election datasource
class VoterInfoViewModelFactory (
    private val database: ElectionDao,
    private val electionId: Int,
    private val division: Division,
    private val repository : ElectionsRepository
    )
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java))
        {
            return VoterInfoViewModel(database,electionId,division,repository) as T
        }
        throw IllegalArgumentException("Unable to construct")
    }

}