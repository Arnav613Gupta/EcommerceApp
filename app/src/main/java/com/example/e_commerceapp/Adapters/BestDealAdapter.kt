package com.example.e_commerceapp.Adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.e_commerceapp.data.Product
import com.example.e_commerceapp.databinding.BestDealRvItemBinding
import com.example.e_commerceapp.databinding.SpecialRvItemBinding

class BestDealAdapter:RecyclerView.Adapter<BestDealAdapter.BestDealViewHolder>() {
    inner class BestDealViewHolder(private val binding:BestDealRvItemBinding) :ViewHolder(binding.root){
        fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgBestDeal)

                tvDealProductName.text=product.name
                tvOldPrice.text="$ ${String.format("%.2f",product.price)}"

                product.offerPercentage?.let {
                    val remainperce=1f-product.offerPercentage
                    val newPrice=product.price * remainperce
                    tvNewPrice.text="$ ${String.format("%.2f",newPrice)}"
                    tvOldPrice.paintFlags= Paint.STRIKE_THRU_TEXT_FLAG
                }




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

    val differ= AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestDealViewHolder {
         return BestDealViewHolder(
            BestDealRvItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BestDealViewHolder, position: Int) {
        val product=differ.currentList[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    var onClick:((Product)->Unit)?=null
}