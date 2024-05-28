package com.example.e_commerceapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerceapp.databinding.SizeRvItemBinding

class SizeAdapter: RecyclerView.Adapter< SizeAdapter. SizeViewHolder>() {

    private var selectedPosition=-1
    inner class  SizeViewHolder (private val binding: SizeRvItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sizeCode:String,position: Int) {

            binding.tvSize.text=sizeCode
            if(position==selectedPosition){
                binding.apply {
                    imageShadow.visibility= View.VISIBLE

                }

            }else{
                binding.apply {
                    imageShadow.visibility= View.INVISIBLE


                }

            }


        }
    }


    private val differCallback=object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

    }
    val differ= AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  SizeViewHolder {
        return  SizeViewHolder(SizeRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder:  SizeViewHolder, position: Int) {
        val size=differ.currentList[position]
        holder.bind(size,position)

        holder.itemView.setOnClickListener{

            if(selectedPosition>=0){
                notifyItemChanged(selectedPosition)
            }

            selectedPosition=holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(size)
        }
    }

    var onItemClick:((String)->Unit)?=null
}