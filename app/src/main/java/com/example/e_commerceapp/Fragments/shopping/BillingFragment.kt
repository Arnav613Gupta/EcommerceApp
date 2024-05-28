package com.example.e_commerceapp.Fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerceapp.Adapters.AddressAdapter
import com.example.e_commerceapp.Adapters.BillingProductsAdapter
import com.example.e_commerceapp.R
import com.example.e_commerceapp.Utils.HorizontalItemDecoratiojn
import com.example.e_commerceapp.Utils.Resourse
import com.example.e_commerceapp.ViewModel.BillingViewMOdel
import com.example.e_commerceapp.ViewModel.OrderViewModel
import com.example.e_commerceapp.data.Address
import com.example.e_commerceapp.data.CartProduct
import com.example.e_commerceapp.data.order.OrderStatus
import com.example.e_commerceapp.databinding.FragmentBillingBinding
import com.example.e_commerceapp.data.order.Order
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class BillingFragment : Fragment() {

    private lateinit var binding:FragmentBillingBinding
    private val viewModel by viewModels<BillingViewMOdel>()
    private  val addressAdapter by lazy {AddressAdapter()}
    private  val billingProductAdapter by lazy {BillingProductsAdapter()}

    private val args by navArgs<BillingFragmentArgs>()
    private var products= emptyList<CartProduct>()
    private var totalPrice=0f

    private var selectedAddress:Address?=null
    private val orderViewModel by viewModels <OrderViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        products=args.cartProduct.toList()
        totalPrice=args.totalPrice
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding= FragmentBillingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBillingProductRv()
        setUpAddressRv()


        if(!args.payment){

            binding.apply {
                buttonPlaceOrder.visibility=View.INVISIBLE
                totalBoxContainer.visibility=View.INVISIBLE
                middleLine.visibility=View.INVISIBLE
                bottomLine.visibility=View.INVISIBLE
            }
        }


        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }

        binding.imageCloseBilling.setOnClickListener {
            findNavController().navigateUp()
        }

        lifecycleScope.launch {
            viewModel.address.collectLatest {
                when(it){
                    is Resourse.Error -> {
                        binding.progressbarAddress.visibility=View.INVISIBLE
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                    }
                    is Resourse.Loading -> {
                        binding.progressbarAddress.visibility=View.VISIBLE
                    }
                    is Resourse.Success -> {
                        binding.progressbarAddress.visibility=View.INVISIBLE
                        addressAdapter.differ.submitList(it.data)
                    }
                    is Resourse.Unspecified -> Unit
                }
            }
        }


        lifecycleScope.launch {
            orderViewModel.order.collectLatest {
                when(it){
                    is Resourse.Error -> {
                        binding.buttonPlaceOrder.revertAnimation()
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                    }
                    is Resourse.Loading -> {
                        binding.buttonPlaceOrder.startAnimation()
                    }
                    is Resourse.Success -> {
                        binding.buttonPlaceOrder.revertAnimation()
                        findNavController().navigateUp()
                        Snackbar.make(requireView(),"Oredered Placed Successfully",Snackbar.LENGTH_LONG).show()
                    }
                    is Resourse.Unspecified -> Unit
                }
            }
        }
        billingProductAdapter.differ.submitList(products)
        binding.tvTotalPrice.text=totalPrice.toString()

        addressAdapter.onClick={
            selectedAddress=it

            val b=Bundle().apply { putParcelable("address",selectedAddress) }
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment,b)
        }

        binding.buttonPlaceOrder.setOnClickListener {
            if(selectedAddress==null){
                Toast.makeText(requireContext(),"Please select Address",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            showOrderConfirmationDialog()


        }
    }

    private fun showOrderConfirmationDialog() {
        val alertDialog= AlertDialog.Builder(requireContext()).apply {
            setTitle("Order Items.")
            setMessage("Are you sure to order these products")
            setNegativeButton("Cancel"){dialog,_->
                dialog.dismiss()
            }
            setPositiveButton("Sure"){dialog,_->
                val orderProducts=Order(OrderStatus.Ordered.status,totalPrice,products,selectedAddress!!)

                orderViewModel.placeOrder(orderProducts)
                dialog.dismiss()

            }
        }
        alertDialog.create().show()
    }

    private fun setUpAddressRv() {
        binding.rvAddress.apply {
            layoutManager=LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
            adapter=addressAdapter
            addItemDecoration(HorizontalItemDecoratiojn())

        }
    }

    private fun setUpBillingProductRv() {
        binding.rvProducts.apply {
            layoutManager=LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
            adapter=billingProductAdapter
            addItemDecoration(HorizontalItemDecoratiojn())
        }
    }


}