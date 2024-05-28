package com.example.e_commerceapp.Fragments.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.OnScrollListener
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerceapp.Adapters.BestDealAdapter
import com.example.e_commerceapp.Adapters.BestProductAdapter
import com.example.e_commerceapp.R
import com.example.e_commerceapp.Utils.showBottomNavitaion
import com.example.e_commerceapp.databinding.FragmentBaseCategoryBinding


open class BaseCategoryFragment : Fragment() {
    private lateinit var binding: FragmentBaseCategoryBinding
     protected val bestProductrAdapter: BestProductAdapter by lazy { BestProductAdapter() }
     protected val offerProductrAdapter: BestProductAdapter by lazy { BestProductAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        OfferProduct()
        setUpBestProduct()


        binding.rvOfferProduct.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!recyclerView.canScrollHorizontally(1)&& dx!=0){
                    onOfferPagingRequest()
                }
            }
        })

        binding.nestedScrolllBaseCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{ v, _, scrollY, _, _->
            if(v.getChildAt(0).bottom<=v.height+scrollY){
                onBestProductPagingRequest()
            }


        })


        bestProductrAdapter.onClick={
            val b=Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment,b)
        }

        offerProductrAdapter.onClick={
            val b=Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment,b)
        }



    }

    open fun onOfferPagingRequest(){

    }
    open fun onBestProductPagingRequest(){

    }

    fun showOfferLoading(){
        binding.ProgressBarrofferBaseFrag.visibility=View.VISIBLE
    }
    fun hideOfferLoading(){
        binding.ProgressBarrofferBaseFrag.visibility=View.INVISIBLE
    }
    fun showBestProductLoading(){
        binding.ProgressBarrBestBaseFrag.visibility=View.VISIBLE

    }
    fun hideBestProductLoading() {
        binding.ProgressBarrBestBaseFrag.visibility=View.INVISIBLE

    }







    private fun setUpBestProduct() {
        binding.rvBestProducts.apply {


            layoutManager= GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)
            adapter=bestProductrAdapter

        }
    }

    private fun OfferProduct() {
        binding.rvOfferProduct.apply {


            layoutManager=  LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter=offerProductrAdapter

        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavitaion()
    }
}