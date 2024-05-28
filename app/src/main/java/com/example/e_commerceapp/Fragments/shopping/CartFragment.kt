package com.example.e_commerceapp.Fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerceapp.Adapters.CartProductAdapter
import com.example.e_commerceapp.Firebase.FireBaseCommon

import com.example.e_commerceapp.R
import com.example.e_commerceapp.Utils.Resourse
import com.example.e_commerceapp.Utils.VerticalItemDecoratiojn
import com.example.e_commerceapp.ViewModel.CartViewModel
import com.example.e_commerceapp.databinding.FragmentCartBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CartFragment : Fragment() {

    private lateinit var binding:FragmentCartBinding
    private val cartAdapter by lazy{CartProductAdapter()}
    private val viewModel by activityViewModels<CartViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpCartRv()

        var totalPrice=0f
        binding.buttonCheckout.setOnClickListener {
            val action=CartFragmentDirections.actionCartFragmentToBillingFragment(totalPrice, cartAdapter.differ.currentList.toTypedArray(),true)
            findNavController().navigate(action)
        }


        lifecycleScope.launch {
            viewModel.productPrice.collectLatest { price->
                price?.let {
                    totalPrice=it
                    binding.tvTotalPrice.text="$ $price"
                }
            }
        }
        cartAdapter.onProductClick={
            val b=Bundle(). apply{
                putParcelable("product",it.product)
            }
            findNavController().navigate(R.id.action_cartFragment_to_productDetailFragment,b)
        }


        cartAdapter.onPlusClick={
            viewModel.changeQuantity(it,FireBaseCommon.quantityChanging.INCREASE)
        }

        cartAdapter.onMinusClick={
            viewModel.changeQuantity(it,FireBaseCommon.quantityChanging.DECREASE)
        }

        lifecycleScope.launch {
            viewModel.deleteDialog.collectLatest {
                val alertDialog=AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete Product")
                    setMessage("Are you sure to delete this product")
                    setNegativeButton("Cancel"){dialog,_->
                        dialog.dismiss()
                    }
                    setPositiveButton("Sure"){dialog,_->
                        viewModel.deleteCartProduct(it)
                        dialog.dismiss()

                    }
                }
                alertDialog.create().show()
            }
        }

        binding.imageCloseCart.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_homeFragment)
        }


        lifecycleScope.launch {
            viewModel.cartProducts.collectLatest {
                when(it){
                    is Resourse.Error -> {

                        binding.progressbarCart.visibility=View.INVISIBLE
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                    }
                    is Resourse.Loading -> {
                        binding.progressbarCart.visibility=View.VISIBLE
                    }
                    is Resourse.Success -> {
                        if(it.data!!.isEmpty()){
                            showEmptyCart()
                            hideOtherView()
                        }else{
                            hideEmptyCart()
                            showOtherView()
                            cartAdapter.differ.submitList(it.data)
                        }

                        binding.progressbarCart.visibility=View.INVISIBLE
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun showOtherView() {

        binding.apply {
            rvCart.visibility=View.VISIBLE
            totalBoxContainer.visibility=View.VISIBLE
            buttonCheckout.visibility=View.VISIBLE
        }
    }

    private fun hideOtherView() {
        binding.apply {
            rvCart.visibility=View.INVISIBLE
            totalBoxContainer.visibility=View.INVISIBLE
            buttonCheckout.visibility=View.INVISIBLE
        }


    }

    private fun hideEmptyCart() {
        binding.apply {
            layoutCarEmpty.visibility=View.GONE
        }

    }

    private fun showEmptyCart() {
        binding.apply {
            layoutCarEmpty.visibility=View.VISIBLE
        }

    }

    private fun setUpCartRv() {
       binding.rvCart.apply {
           layoutManager =LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
           adapter=cartAdapter
           addItemDecoration(VerticalItemDecoratiojn())
       }
    }

}