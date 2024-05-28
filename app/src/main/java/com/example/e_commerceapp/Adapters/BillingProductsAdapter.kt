package com.example.e_commerceapp.Adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerceapp.Helper.getProductPrice
import com.example.e_commerceapp.data.Address
import com.example.e_commerceapp.data.CartProduct
import com.example.e_commerceapp.databinding.AddressRvItemBinding
import com.example.e_commerceapp.databinding.BillingProductRvItemBinding

class BillingProductsAdapter:RecyclerView.Adapter<BillingProductsAdapter.BillingProductsViewHolder>() {
    inner class BillingProductsViewHolder(val binding: BillingProductRvItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(billingProduct: CartProduct){
        binding.apply {
            Glide.with(itemView).load(billingProduct.product.images[0]).into(imageCartProduct)
            tvProductCartName.text=billingProduct.product.name
            tvBillingProductQuantity.text=billingProduct.quantity.toString()

            val priceAfterPercentage=billingProduct.product.offerPercentage.getProductPrice(billingProduct.product.price)
            tvProductCartPrice.text="$ ${String.format("%.2f",priceAfterPercentage)}"

            imageCartProductColor.setImageDrawable(ColorDrawable(billingProduct.selectedColor?: Color.TRANSPARENT))
            tvCartProductSize.text=billingProduct.selectedSize ?: "".also {
                imageCartProductSize.setImageDrawable(ColorDrawable(Color.TRANSPARENT)) }


        }


    }
    }


    private val diffCallback= object : DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id==newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem==newItem
        }

    }

    val differ= AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingProductsViewHolder {
        return BillingProductsViewHolder(BillingProductRvItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BillingProductsViewHolder, position: Int) {
        val billingProduct=differ.currentList[position]

        holder.bind(billingProduct)
    }


}