package com.example.e_commerceapp.Adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerceapp.databinding.ColorRvItemBinding
import com.example.e_commerceapp.databinding.ViewpagerImageItemBinding

class ColorsAdapter: RecyclerView.Adapter<ColorsAdapter.ColorViewHolder>() {
    private var selectedPosition=-1
    inner class ColorViewHolder (private val binding: ColorRvItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(colorCode:Int,position: Int) {

            val imageDrawable=ColorDrawable(colorCode)
            binding.imageColor.setImageDrawable(imageDrawable)
            if(position==selectedPosition){
                binding.apply {
                    imageShadow.visibility= View.VISIBLE
                    imagePicker.visibility=View.VISIBLE

                }

            }else{
                binding.apply {
                    imageShadow.visibility= View.INVISIBLE
                    imagePicker.visibility=View.INVISIBLE

                }

            }


        }
    }


    private val differCallback=object : DiffUtil.ItemCallback<Int>(){
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem==newItem
        }

    }
    val differ= AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder(ColorRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val color=differ.currentList[position]
        holder.bind(color,position)

        holder.itemView.setOnClickListener{

            if(selectedPosition>=0){
                notifyItemChanged(selectedPosition)
            }

            selectedPosition=holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(color)
        }
    }

    var onItemClick:((Int)->Unit)?=null
}