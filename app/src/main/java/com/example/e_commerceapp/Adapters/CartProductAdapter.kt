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
import com.example.e_commerceapp.data.CartProduct
import com.example.e_commerceapp.databinding.CardProductLayoutBinding

class CartProductAdapter: RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder>() {
    inner class CartProductViewHolder(val binding: CardProductLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(cartProduct: CartProduct) {
            binding.apply {
                Glide.with(itemView).load(cartProduct.product.images[0]).into(imageCartProduct)

                tvProductCartName.text=cartProduct.product.name
                cartProductQuantity.text=cartProduct.quantity.toString()


                val priceAfterPercentage=cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price)
                tvProductCartPrice.text="$ ${String.format("%.2f",priceAfterPercentage)}"

                imagecartproductcolor.setImageDrawable(ColorDrawable(cartProduct.selectedColor?:Color.TRANSPARENT))
                tvcartproductsize.text=cartProduct.selectedSize ?: "".also {
                    imagecartproductsize.setImageDrawable(ColorDrawable(Color.TRANSPARENT)) }

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




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        return CartProductViewHolder(
            CardProductLayoutBinding.inflate(
                LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        val cartProduct=differ.currentList[position]
        holder.bind(cartProduct)
        holder.itemView.setOnClickListener{
            onProductClick?.invoke(cartProduct)
        }

        holder.binding.imagePlus.setOnClickListener{
            onPlusClick?.invoke(cartProduct)
        }

        holder.binding.imageMinus.setOnClickListener{
            onMinusClick?.invoke(cartProduct)
        }
    }

    var onProductClick:((CartProduct)->Unit)?=null
    var onPlusClick:((CartProduct)->Unit)?=null
    var onMinusClick:((CartProduct)->Unit)?=null
}
