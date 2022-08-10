package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ElectionItemBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(
    private val clickListener: ElectionListener):
    ListAdapter<Election,ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    //TODO: Create ElectionViewHolder
    class ElectionViewHolder(private var binding: ElectionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(election: Election, clickListener: ElectionListener) {
            binding.election = election
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }//End bind

        //TODO: Add companion object to inflate ViewHolder (from)
        companion object {
            fun from(parent: ViewGroup): ElectionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ElectionItemBinding.inflate(layoutInflater, parent, false)
                return ElectionViewHolder(binding)
            }
        }
    }

    //TODO: Bind ViewHolder
    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val election = getItem(position)
        holder.bind(election,clickListener)
        holder.itemView.setOnClickListener{
            clickListener.onClick(election)
        }

    }

}//End of adapter

    //TODO: Create ElectionListener
    class ElectionListener(val clickListener: (election: Election) -> Unit) {
        fun onClick(election: Election) = clickListener(election)
}
    //TODO: Create ElectionDiffCallback
    class ElectionDiffCallback : DiffUtil.ItemCallback<Election>()
    {
        override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem === newItem }
        override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem.id == newItem.id }
    }

