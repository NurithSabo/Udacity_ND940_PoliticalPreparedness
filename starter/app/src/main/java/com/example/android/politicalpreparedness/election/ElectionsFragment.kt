package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener

class ElectionsFragment: Fragment() {

    //TODO: Add binding values
    //TODO: Initiate recycler adapters
    //TODO: Populate recycler adapters
    //TODO: Link elections to voter info
    //TODO: Add ViewModel values and create ViewModel
    //TODO: Refresh adapters when fragment loads
    //TODO: Declare ViewModel
    private lateinit var viewModel: ElectionsViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val application = requireNotNull(this.activity).application
        val electionDao = ElectionDatabase.getInstance(application).electionDao
        val repository = ElectionsRepository(electionDao)
        val viewModelFactory = ElectionsViewModelFactory(repository)

        viewModel = ViewModelProvider(this,viewModelFactory).get(ElectionsViewModel::class.java)
        //viewModel._dataLoading.value = true

        val binding =FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.electionsViewmodel = viewModel

        val upcomingElectionsAdapter = ElectionListAdapter(
            ElectionListener{
                findNavController().navigate(
                    ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                        it.id,
                        it.division
                    )
                )
            })
        val savedElectionAdapter = ElectionListAdapter(
            ElectionListener {
                findNavController().navigate(
                    ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                        it.id,
                        it.division
                    )
                )
            }
        )

        binding.upcomingRecy.adapter = upcomingElectionsAdapter
        binding.savedRecy.adapter = savedElectionAdapter

        viewModel.upcomingElections.observe(viewLifecycleOwner){
            it.let {
                upcomingElectionsAdapter.submitList(it)
            }
        }
        viewModel.savedElections.observe(viewLifecycleOwner) {
                it.let {
                    savedElectionAdapter.submitList(it)
                }
            }
        return binding.root
    }
//  Refresh
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSavedElections()
    }
}