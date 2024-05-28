package com.example.e_commerceapp.Adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.e_commerceapp.R
import com.example.e_commerceapp.ViewModel.OrderViewModel
import com.example.e_commerceapp.data.order.Order
import com.example.e_commerceapp.data.order.OrderStatus
import com.example.e_commerceapp.data.order.getOrderedStatus
import com.example.e_commerceapp.databinding.FragmentOrdersBinding
import com.example.e_commerceapp.databinding.OrderItemBinding

class OrdersAdapter:RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {
   inner class OrderViewHolder (private val binding:OrderItemBinding):ViewHolder(binding.root){
       fun bind(order: Order){

           binding.apply {
               tvOrderId.text=order.orderId.toString()
               tvOrderDate.text=order.date
               val resources=itemView.resources


               val colorDrawable=when(getOrderedStatus(order.orderStatus)){
                   OrderStatus.Canceled -> ColorDrawable(resources.getColor(R.color.g_red))
                   OrderStatus.Confirmed -> ColorDrawable(resources.getColor(R.color.g_green))
                   OrderStatus.Delivered -> ColorDrawable(resources.getColor(R.color.g_green))
                   OrderStatus.Ordered ->     ColorDrawable(resources.getColor(R.color.g_orange_yellow))
                   OrderStatus.Returned ->  ColorDrawable(resources.getColor(R.color.g_red))
                   OrderStatus.Shiped ->    ColorDrawable(resources.getColor(R.color.g_green))
               }


               imageOrderState.setImageDrawable(colorDrawable)

           }

       }
   }

    val diffUtil=object :DiffUtil.ItemCallback<Order>(){
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.product==newItem.product
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
           return oldItem==newItem
        }

    }

    val differ=AsyncListDiffer(this,diffUtil)




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return  OrderViewHolder(OrderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
       val order=differ.currentList[position]

        holder.bind(order)

        holder.itemView.setOnClickListener {
            onClick?.invoke(order)
        }

    }
    var onClick :((Order)->Unit)?=null

}

