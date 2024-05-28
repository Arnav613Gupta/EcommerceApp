package com.example.e_commerceapp.Fragments.shopping

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerceapp.Activity.ShopingActivity
import com.example.e_commerceapp.Adapters.ColorsAdapter
import com.example.e_commerceapp.Adapters.ViewPager2Images
import com.example.e_commerceapp.Adapters.SizeAdapter
import com.example.e_commerceapp.R
import com.example.e_commerceapp.Utils.Resourse
import com.example.e_commerceapp.Utils.hideBottomNavitaion
import com.example.e_commerceapp.ViewModel.DetailViewModel
import com.example.e_commerceapp.data.CartProduct
import com.example.e_commerceapp.databinding.FragmentProductDetailBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private val args by navArgs<ProductDetailFragmentArgs>()

    private lateinit var binding:FragmentProductDetailBinding
    private val viewPagerAdapter by lazy { ViewPager2Images() }
    private val sizeAdapter by lazy { SizeAdapter() }
    private val colorAdapter by lazy { ColorsAdapter() }
    private var selectedColor:Int?=null
    private var selectedSize:String?=null
    private val viewModel by viewModels<DetailViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentProductDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideBottomNavitaion()


        val product=args.product

        setUpSizesRv()
        setUpColorRv()
        setUpViewPager()



        binding.imageClose.setOnClickListener {
            findNavController().navigateUp()
        }

        sizeAdapter.onItemClick={
            selectedSize=it
        }
        colorAdapter.onItemClick={
            selectedColor=it
        }

        binding.btnAddToCart.setOnClickListener {
            viewModel.addUpdateProductInCart(CartProduct(product,1,selectedColor,selectedSize))
        }

        lifecycleScope.launch {
            viewModel.addToCart.collectLatest {
                when(it){
                    is Resourse.Error -> {
                        binding.btnAddToCart.revertAnimation()
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                    }
                    is Resourse.Loading -> binding.btnAddToCart.startAnimation()
                    is Resourse.Success -> {
                        binding.btnAddToCart.apply {
                            revertAnimation()
                            binding.btnAddToCart.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.black))
                            Toast.makeText(requireContext(),"Added to cart",Toast.LENGTH_SHORT).show()
                        }

                    }
                    is Resourse.Unspecified -> Unit
                }
            }
        }

        binding.apply {

            tvProductName.text=product.name
            tvProductPrice.text="$ ${product.price}"
            tvProductDesc.text=product.description

            if(product.colors.isNullOrEmpty()){
                tvProductColors.visibility=View.GONE
            }

            if(product.sizes.isNullOrEmpty()){
                tvProductSize.visibility=View.GONE
            }

        }
        viewPagerAdapter.differ.submitList(product.images)

        product.colors?.let {
            colorAdapter.differ.submitList(product.colors)
            Log.e("TAG_C",product.colors.toString())
        }

        product.sizes?.let {
            sizeAdapter.differ.submitList(product.sizes)
            Log.e("TAG_S",product.sizes.toString())
        }




    }

    private fun setUpViewPager() {
        binding.apply {
            viewPagerProductImages.adapter=viewPagerAdapter

        }
    }

    private fun setUpColorRv() {
        binding.recyclerVirewClors.apply {
            adapter=colorAdapter
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun setUpSizesRv() {
        binding.recyclerVirewSize.apply {
            adapter=sizeAdapter
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }


}