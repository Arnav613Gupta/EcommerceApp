package com.example.e_commerceapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.e_commerceapp.databinding.ViewpagerImageItemBinding

class ViewPager2Images:RecyclerView.Adapter<ViewPager2Images.ViewPager2AdapterViewHolder>() {
    inner class ViewPager2AdapterViewHolder (private val binding:ViewpagerImageItemBinding):ViewHolder(binding.root) {
        fun bind(imagePath: String) {
            Glide.with(itemView).load(imagePath).into(binding.imageProductDetails)

        }
    }


    private val differCallback=object :DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

    }
    val differ=AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPager2AdapterViewHolder {
        return ViewPager2AdapterViewHolder(ViewpagerImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewPager2AdapterViewHolder, position: Int) {
        val image=differ.currentList[position]
        holder.bind(image)
    }
}