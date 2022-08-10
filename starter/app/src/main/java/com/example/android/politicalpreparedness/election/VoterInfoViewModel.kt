package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.launch
import okhttp3.internal.http2.Http2Connection
import retrofit2.HttpException
import java.net.UnknownHostException

val TAG = "Bogi's Log"

class VoterInfoViewModel(
    private val dataSource: ElectionDao,
    private val electionId: Int,
    private val division: Division,
    private val repository: ElectionsRepository
): ViewModel()

{
    private val _voterInfo =MutableLiveData<VoterInfoResponse?>()
    val voterInfo : LiveData<VoterInfoResponse?>
        get() = _voterInfo
    private val _locationUrl = MutableLiveData<String?>()
    val locationUrl : LiveData<String?>
        get() = _locationUrl
    private val _ballUrl = MutableLiveData<String?>()
    val ballUrl : LiveData<String?>
        get() = _ballUrl
    private val _corrAddress = MutableLiveData<String>()
    val corrAddress :LiveData<String>
        get() = _corrAddress
    private val _hasCorrAddress = MutableLiveData<Boolean>()
    val hasCorrAddress: LiveData<Boolean> = _hasCorrAddress

    private val _hasVoterInfo = MutableLiveData<Boolean>()
    val hasVoterInfo: LiveData<Boolean> = _hasVoterInfo

    private val _networkError = MutableLiveData<Boolean>()
    val networkError: LiveData<Boolean> = _networkError

    private val _hasLocUrl = MutableLiveData<Boolean>()
    val hasLocUrl : LiveData<Boolean> = _hasLocUrl

    private val _hasBallUrl = MutableLiveData<Boolean>()
    val hasBalUrl : LiveData<Boolean>
        get() = _hasBallUrl

    private val _isFollowed = MutableLiveData<Boolean>()
    val isFollowed : LiveData<Boolean>
        get() = _isFollowed

    fun getElection(){
        viewModelScope.launch{

        try {

             val country = division.country
             val state = division.state

        if(state !="") {
            val address = "$country,$state"
            val result = repository.getInfo(address, electionId)

            if (result != null) {
                _voterInfo.value = result
                getCorresAddress()
                hasVotingLocation()
                hasBallUrl()
                _hasVoterInfo.value = true
            }

            else {
                _networkError.value = true
            }
        }
        else {
            _hasVoterInfo.value = false
        }
    }
         catch (e: HttpException) {
             Log.i(TAG,"VoterInfo: "+ "Kaki van a palacsintában")
             _hasVoterInfo.value = false
             _networkError.value = true
        }
        }}

    init {
        getElection()
        checkFollow()
    }

    fun getCorresAddress(){
        _corrAddress.value =
             _voterInfo.value?.state?.get(0)?.electionAdministrationBody?.correspondenceAddress?.toFormattedString()
        _hasCorrAddress.value = _corrAddress.value != null
    }

    fun getVotingLocationUrl(){
        _locationUrl.value = _voterInfo.value?.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl
    }
    fun hasVotingLocation() : MutableLiveData<Boolean> {
        if (_voterInfo.value?.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl != null){
            _hasLocUrl.value = true
        }
        else{
            _hasLocUrl.value = false
        }
        return _hasLocUrl
    }

    fun getBallotInformationUrl(){
        _ballUrl.value = _voterInfo.value?.state?.get(0)?.electionAdministrationBody?.electionInfoUrl
    }
    fun hasBallUrl() : MutableLiveData<Boolean>{
        if (_voterInfo.value?.state?.get(0)?.electionAdministrationBody?.electionInfoUrl != null){
            _hasBallUrl.value = true
        }
        else{
            _hasBallUrl.value = false
        }
        return _hasBallUrl
    }


    fun checkFollow(){
        viewModelScope.launch {

            _isFollowed.value = repository.isElectionSaved(electionId)
            Log.i(TAG, "? "+_isFollowed.value.toString())
        }
    }

    fun followUnfollowElection(election: Election, isFollowed : Boolean){
        viewModelScope.launch {
        //election = dataSource.getElection(electionId).value as Election
        if(!isFollowed){
            repository.saveElection(election)
            _isFollowed.value = true
            Log.i(TAG, "saved")
        }
        else{
            repository.deleteElection(electionId)
            _isFollowed.value = false
            Log.i(TAG,"deleted")
        }
        }
    }

    //TODO: Add live data to hold voter info
    //TODO: Add var and methods to populate voter info
    //TODO: Add var and methods to support loading URLs
    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status
    /**
     * Hint: The saved state can be accomplished in multiple ways.
     * It is directly related to how elections are saved/removed from the database.
     */
}