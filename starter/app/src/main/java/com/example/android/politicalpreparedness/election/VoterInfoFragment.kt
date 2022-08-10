package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_voter_info.view.*


class VoterInfoFragment : Fragment() {
    private lateinit var viewModel: VoterInfoViewModel

    private val args: VoterInfoFragmentArgs by navArgs()
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val application = requireNotNull(this.activity).application
        val electionDao = ElectionDatabase.getInstance(application).electionDao
        val repository = ElectionsRepository(electionDao)
        val bundle = VoterInfoFragmentArgs.fromBundle(requireArguments())
        val division = bundle.argDivision
        val electionId = bundle.argElectionId
        val viewModelFactory = VoterInfoViewModelFactory(electionDao,electionId,division,repository)



        viewModel = ViewModelProvider(this,viewModelFactory).get(VoterInfoViewModel::class.java)
        binding.voterInfoVm = viewModel

        viewModel.hasVoterInfo.observe(viewLifecycleOwner){
        hasInfo ->
        if(hasInfo) {
            visibility(true, binding)}
        else{
            visibility(false,binding)
            }}

        viewModel.hasVoterInfo.observe(viewLifecycleOwner){
                hasInfo ->
            if(!hasInfo) {
                binding.iLetter.visibility = View.VISIBLE
                binding.infoText.visibility = View.VISIBLE
            }
        }

        viewModel.hasCorrAddress.observe(viewLifecycleOwner){
            hasCorrAddress ->
            if(hasCorrAddress){
                binding.stateCorrespondenceHeader.text = "Correspondence address:"
            }
        }

        viewModel.hasLocUrl.observe(viewLifecycleOwner){
                hasLocUrl ->
            if(hasLocUrl){
                binding.stateLocations.text = "Voting Locations ->"
                binding.stateHeader.text = "Election information!!!"
            }
        }
        viewModel.hasBalUrl.observe(viewLifecycleOwner){
                hasBalUrl ->
            if(hasBalUrl){
                binding.stateBallot.text = "Ballot information ->"
                binding.stateHeader.text = "Election information"
            }
        }

        viewModel.locationUrl.observe(viewLifecycleOwner){
            url ->
            if(url != null){
                loadUrlIntents(url)
                Log.i(TAG, "url: "+url.toString())
            }
        }
        viewModel.ballUrl.observe(viewLifecycleOwner){
                url ->
            if(url != null){
                loadUrlIntents(url)
                Log.i(TAG, "url: "+url.toString())
            }
        }
        return  binding.root
    }

    //https://stackoverflow.com/questions/2201917/how-can-i-open-a-url-in-androids-web-browser-from-my-application
    fun loadUrlIntents(url : String){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)}
    }

    fun ButtonText (savedOrNot : Boolean, binding: FragmentVoterInfoBinding){
        if(savedOrNot){
            binding.followElectionButton.text = "Unfollow election"
        }
        else{
            binding.followElectionButton.text = "FOLLOW ELECTION"
        }
    }

    fun visibility(hasOrHasnt : Boolean, binding : FragmentVoterInfoBinding)
    {binding.run {

        if (hasOrHasnt == true) {
            stateHeader.visibility = View.VISIBLE
            stateBallot.visibility = View.VISIBLE
            stateCorrespondenceHeader.visibility = View.VISIBLE
            address.visibility = View.VISIBLE
            stateLocations.visibility = View.VISIBLE
           // followElectionButton.text = "FOLLOW ELECTION"

        } else {
            stateHeader.visibility = View.INVISIBLE
            stateBallot.visibility = View.INVISIBLE
            stateCorrespondenceHeader.visibility = View.INVISIBLE
            address.visibility = View.INVISIBLE
            stateLocations.visibility = View.INVISIBLE
            followElectionButton.visibility = View.INVISIBLE
        }
    }
    }

    //TODO: Create method to load URL intents
    //TODO: Add ViewModel values and create ViewModel
    //TODO: Add binding values
    //TODO: Populate voter info -- hide views without provided data.
    /**
    Hint: You will need to ensure proper data is provided from previous fragment.
     */
    //TODO: Handle loading of URLs
    //TODO: Handle save button UI state
    //TODO: cont'd Handle save button clicks