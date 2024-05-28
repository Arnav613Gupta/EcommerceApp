package com.example.e_commerceapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerceapp.Adapters.BillingProductsAdapter
import com.example.e_commerceapp.R
import com.example.e_commerceapp.Utils.VerticalItemDecoratiojn
import com.example.e_commerceapp.data.order.OrderStatus
import com.example.e_commerceapp.data.order.getOrderedStatus
import com.example.e_commerceapp.databinding.FragmentOrderDetailBinding

class OrderDetailFragmentFragment : Fragment() {
  private lateinit var binding:FragmentOrderDetailBinding
  private val billingProductAdapter by lazy { BillingProductsAdapter() }
    private val args by navArgs<OrderDetailFragmentFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentOrderDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val order =args.order
        setUpOrderRv()

        binding.apply {

            tvOrderId.text="Order #${order.orderId}"

            stepView.setSteps(
                mutableListOf(
                    OrderStatus.Ordered.status,
                    OrderStatus.Confirmed.status,
                    OrderStatus.Shiped.status,
                    OrderStatus.Delivered.status
                )
            )

            val orderCurrentStatus=when(getOrderedStatus(order.orderStatus)){
                OrderStatus.Ordered -> 0
                OrderStatus.Confirmed -> 1
                OrderStatus.Shiped -> 2
                OrderStatus.Delivered -> 3
                else->0

            }

            stepView.go(orderCurrentStatus,false)

            if(orderCurrentStatus==3){
                stepView.done(true)
            }

            tvFullName.text=order.address.fullName

            tvAddress.text="${order.address.street} ${order.address.city} "
            tvPhoneNumber.text=order.address.phoneNo


            tvTotalPrice.text= "$${order.price.toString()}"

        }
        billingProductAdapter.differ.submitList(order.product)
    }

    private fun setUpOrderRv() {

        binding.rvProducts.apply {
            adapter=billingProductAdapter
            layoutManager=LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            addItemDecoration(VerticalItemDecoratiojn())

        }
    }

}