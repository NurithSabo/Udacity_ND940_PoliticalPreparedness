package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsRepository constructor(val electionDao: ElectionDao){
    // api methods
    suspend fun getUpcomingElections(): ElectionResponse =
        withContext(Dispatchers.IO) {
            CivicsApi.retrofitService.getElections()
        }

    suspend fun getSavedElectionsList(): List<Election> =
        withContext(Dispatchers.IO) {
            electionDao.getElections()
        }

    suspend fun getInfo(address: String, electionId: Int): VoterInfoResponse =
        withContext(Dispatchers.IO){
            CivicsApi.retrofitService.getVoterInfo(electionId,address)
        }

    suspend fun saveElection(election : Election){
        withContext(Dispatchers.IO){
            electionDao.insertAll(election)
        }
    }
    suspend fun deleteElection(electionId : Int){
        withContext(Dispatchers.IO){
            electionDao.deleteElection(electionId)
        }
    }

    suspend fun isElectionSaved(electionId : Int) : Boolean{
         return withContext(Dispatchers.IO){
            electionDao.exists(electionId)
        }
    }
}