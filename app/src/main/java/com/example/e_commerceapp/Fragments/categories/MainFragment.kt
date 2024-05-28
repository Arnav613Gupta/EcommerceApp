package com.example.e_commerceapp.Fragments.categories

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerceapp.Adapters.BestDealAdapter
import com.example.e_commerceapp.Adapters.BestProductAdapter
import com.example.e_commerceapp.Adapters.SpecialProductorAdapter
import com.example.e_commerceapp.R
import com.example.e_commerceapp.Utils.Resourse
import com.example.e_commerceapp.Utils.showBottomNavitaion
import com.example.e_commerceapp.ViewModel.MainCategoryViewModel
import com.example.e_commerceapp.databinding.FragmentMainBinding
import com.example.e_commerceapp.databinding.SpecialRvItemBinding
import com.google.firebase.database.collection.LLRBNode.Color
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

        private lateinit var binding: FragmentMainBinding
        private lateinit var specialProductorAdapter: SpecialProductorAdapter
        private lateinit var bestProductrAdapter: BestProductAdapter
        private lateinit var bestDealAdapter: BestDealAdapter

        private val viewModel by viewModels<MainCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding=FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpSpecialRv()
        setUpBestDealRv()
        setUpBestProductRv()

        specialProductorAdapter.onClick={
            val b=Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment,b)
        }

        bestDealAdapter.onClick={
            val b=Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment,b)
        }
        bestProductrAdapter.onClick={
            val b=Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment,b)
        }



        lifecycleScope.launch {
            viewModel.specialProduct.collectLatest {
                when(it){
                    is Resourse.Error -> {
                        hideLoading()
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()

                    }
                    is Resourse.Loading -> {
                        showLoading()

                    }
                    is Resourse.Success -> {
                        Log.d("TAg",it.data.toString())
                        specialProductorAdapter.differ.submitList(it.data)
                        hideLoading()


                    }
                    else->Unit
                }
            }
        }







        lifecycleScope.launch {
            viewModel.bestDeals.collectLatest {
                when(it){
                    is Resourse.Error -> {
                        hideLoading()
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()

                    }
                    is Resourse.Loading -> {
                        showLoading()

                    }
                    is Resourse.Success -> {
                        Log.d("TAg",it.data.toString())
                        bestDealAdapter.differ.submitList(it.data)
                        hideLoading()


                    }
                    else->Unit
                }
            }
        }







        lifecycleScope.launch {
            viewModel.bestProducts.collectLatest {
                when(it){
                    is Resourse.Error -> {
                        binding.ProgressBarrbestProductor.visibility=View.GONE
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()

                    }
                    is Resourse.Loading -> {
                        binding.ProgressBarrbestProductor.visibility=View.VISIBLE


                    }
                    is Resourse.Success -> {
                        Log.d("TAg",it.data.toString())
                        bestProductrAdapter.differ.submitList(it.data)
                        binding.ProgressBarrbestProductor.visibility=View.GONE


                    }
                    else->Unit
                }
            }
        }
        binding.nestedScrolllMainCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{v,_,scrollY,_,_->
            if(v.getChildAt(0).bottom<=v.height+scrollY){
                viewModel.fetchBestProduct()
            }


        })
    }

    private fun setUpBestProductRv() {
        bestProductrAdapter= BestProductAdapter()
        binding.rvBestProducts.apply {


            layoutManager=GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
            adapter=bestProductrAdapter

        }
    }

    private fun setUpBestDealRv() {
        bestDealAdapter= BestDealAdapter()
        binding.rvBestDeals.apply {


            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter=bestDealAdapter

        }
    }

    private fun showLoading() {
        binding.ProgressBarrMainFrag.visibility=View.VISIBLE
    }

    private fun hideLoading() {
        binding.ProgressBarrMainFrag.visibility=View.INVISIBLE
    }

    private fun setUpSpecialRv() {

        specialProductorAdapter= SpecialProductorAdapter()
        binding.rvSpecialProduct.apply {


            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter=specialProductorAdapter

        }


    }

    override fun onResume() {
        super.onResume()

        showBottomNavitaion()
    }
}