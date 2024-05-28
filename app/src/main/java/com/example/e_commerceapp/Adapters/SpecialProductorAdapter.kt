package com.example.e_commerceapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.e_commerceapp.data.Product
import com.example.e_commerceapp.databinding.SpecialRvItemBinding

class SpecialProductorAdapter: RecyclerView.Adapter<SpecialProductorAdapter.SpecialProductorViewHolder>() {
   inner class SpecialProductorViewHolder(private val binding:SpecialRvItemBinding ): RecyclerView.ViewHolder(binding.root) {
       fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgSpecialRvItem)

                tvSpecialProductname.text=product.name
                tvSpecialprice.text=product.price.toString()




            }

       }
   }

    private val diffCallback= object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
           return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem==newItem
        }

    }

    val differ=AsyncListDiffer(this,diffCallback)




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductorViewHolder {
        return SpecialProductorViewHolder(
            SpecialRvItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SpecialProductorViewHolder, position: Int) {
        val product=differ.currentList[position]
        holder.bind(product)
        holder.itemView.setOnClickListener{
            onClick?.invoke(product)
        }
    }

    var onClick:((Product)->Unit)?=null
}

