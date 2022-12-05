package com.example.astroid.asteroidradar.main
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.astroid.asteroidradar.domain.Asteroid
import com.example.astroid.databinding.AsteroidItemBinding

class SteroidAdapter (private val clickListener: AsteroidListener) :
    androidx.recyclerview.widget.ListAdapter<Asteroid, SteroidAdapter.AsteroidViewHolder>(SleepNightDiffCallback()) {


    class AsteroidViewHolder(
        private var binding: AsteroidItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid, clickListener: AsteroidListener) {
            binding.asteroid = asteroid
            binding.executePendingBindings()
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AsteroidItemBinding.inflate(layoutInflater, parent, false)
                return AsteroidViewHolder(binding)
            }
        }


    }

    class AsteroidListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder.from(parent) as AsteroidViewHolder

    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }




    class SleepNightDiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

    }
}