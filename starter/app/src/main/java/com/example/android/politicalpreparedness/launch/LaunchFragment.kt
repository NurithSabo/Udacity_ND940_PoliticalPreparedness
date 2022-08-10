package com.example.android.politicalpreparedness.launch

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentLaunchBinding
import com.example.android.politicalpreparedness.election.TAG

class LaunchFragment : Fragment() {
    lateinit var binding: FragmentLaunchBinding
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentLaunchBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.findMyRepresentativesButton.setOnClickListener { navToRepresentatives() }
        binding.upcomingElectionsButton.setOnClickListener { navToElections() }

        return binding.root
    }

//// Listening to the orientation config
//// changes and generating Toast Messages
//override fun onConfigurationChanged(newConfig: Configuration) {
//    super.onConfigurationChanged(newConfig)
//    when (newConfig.orientation) {
//        Configuration.ORIENTATION_LANDSCAPE -> {
//            binding.imageViewSmall.visibility = View.VISIBLE
//            binding.imageView.visibility = View.GONE
//        }
//        Configuration.ORIENTATION_PORTRAIT -> {
//            binding.imageView.visibility = View.VISIBLE
//            binding.imageViewSmall.visibility = View.GONE
//        }
//    }
//}
    private fun navToElections() {
        this.findNavController().navigate(LaunchFragmentDirections.actionLaunchFragmentToElectionsFragment())
    }

    private fun navToRepresentatives() {
        this.findNavController().navigate(LaunchFragmentDirections.actionLaunchFragmentToRepresentativeFragment())
    }

}
