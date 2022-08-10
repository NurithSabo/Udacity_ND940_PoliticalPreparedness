package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.election.TAG
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import java.util.*

class DetailFragment : Fragment() {

    companion object {
    //TODO: Add Constant for Location request
        const val REQUEST_LOCATION_PERMISSION = 1000
    }
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    //TODO: Declare ViewModel
    private val viewModel : RepresentativeViewModel by lazy {
    ViewModelProvider(this).get(RepresentativeViewModel::class.java)
    }
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //TODO: Establish bindings
        val binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        val adapter = RepresentativeListAdapter()
        binding.representativeRecy.adapter = adapter

        viewModel.representatives.observe(viewLifecycleOwner) {
            it.let {
                adapter.submitList(it)
            }
        }

        //TODO: Define and assign Representative adapter
        //TODO: Populate Representative adapter
        //TODO: Establish button listeners for field and location search
        binding.buttonLocation.setOnClickListener {
           if(checkLocationPermissions()){
               getLocation()
               Log.i(TAG, "Permission granted")
           }
            else{
                requestPermission()
           }
        }

        binding.buttonSearch.setOnClickListener {
            val address = Address(
                binding.addressLine1.text.toString(),
                binding.addressLine2.text.toString(),
                binding.city.text.toString(),
                binding.state.selectedItem.toString(),
                binding.zip.text.toString()
            )
            viewModel.getAddress(address,requireContext())

            Log.i(TAG,"line1: "+viewModel.address.value?.line1.toString())
            hideKeyboard()
            viewModel.getAddress(address,requireContext())
            viewModel.getRepresentative()
        }

        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //TODO: Handle location permission result to get location on permission granted
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLocation()
                } else {
                    // Show messages to telling the user why your app actually requires the location permission.
                    // In case they previously chose "Deny & don't ask again",
                    // tell your users where to manually enable the location permission.
                    Snackbar.make(
                        requireView(),
                        R.string.location_permission_denied_explanation,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.settings) {
                            startActivity(Intent().apply {
                                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            })
                        }.show()
                }
            }
        }
    }

    fun requestPermission(){
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION_PERMISSION)
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            requestPermission()
            true
        }
    }

    private fun isPermissionGranted() : Boolean {
        //TODO: Check if permission is already granted and return (true = granted, false = denied/other)
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
        try{
            if (isPermissionGranted()){
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnSuccessListener{
                    location :Location? ->
                    if (location != null ){
                        val address = geoCodeLocation(location)
                        if (address.state.isNotEmpty()){
                            viewModel.getAddress(address,requireContext())
                            viewModel.getRepresentative()
                        }
                    }
                    else{
                    Log.i(TAG,"Location is null!")
                    }
                }
            }
        }
        catch (e : Exception){
            Log.i(TAG, "!!!getLocation: "+e.message)
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
        }
        catch (e :Exception){
            Toast.makeText(requireContext(),"Your location is not in the USA",Toast.LENGTH_LONG).show()
            return Address("","","","","")
        }
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }
}