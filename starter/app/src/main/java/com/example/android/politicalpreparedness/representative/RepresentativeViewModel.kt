package com.example.android.politicalpreparedness.representative

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.election.TAG
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel: ViewModel() {

    //TODO: Establish live data for representatives and address
    private val _address = MutableLiveData<Address>()
    val address : LiveData<Address> =  _address
    //TODO: Create function to fetch representatives from API from a provided address
    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives : LiveData<List<Representative>> =  _representatives

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading


    /**
     *  The following code will prove helpful in constructing a representative from the API.
     *  This code combines the two nodes of the RepresentativeResponse into a single official :
    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }
    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives
     */

    init {
        _address.value = Address("","","","","")
    }

    fun showToast(szoveg : String, context: Context){
        Toast.makeText(context,szoveg, Toast.LENGTH_LONG).show()
    }
    //TODO: Create function get address from geo location
    fun getAddress(address :Address, context: Context){
        try {
            when {
                address.state.isEmpty() -> {
                    showToast("Enter State!",context)
                }
                else -> {
                    _address.value = address
                }
            }
        }
        catch(e: Exception){
            Log.i(TAG, "Something went wrong around the address")
            Log.i(TAG, "Address: "+e.message.toString())
            showToast("Something went wrong at your address",context)
        }
    }

    fun getRepresentative() {
        viewModelScope.launch {
            address.value?.let {
                _loading.setValue(true)
                val (offices, officials) = CivicsApi.retrofitService.getRepresentatives(it.toFormattedString())
                _representatives.value = offices.flatMap { office ->  office.getRepresentatives(officials)}
            }
            _loading.setValue(false)
        }
    }
    //TODO: Create function to get address from individual fields
}
