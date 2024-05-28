package com.example.e_commerceapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerceapp.Adapters.OrdersAdapter
import com.example.e_commerceapp.R
import com.example.e_commerceapp.Utils.Resourse
import com.example.e_commerceapp.ViewModel.OrdersVirewModel
import com.example.e_commerceapp.databinding.FragmentAccountOptionBinding
import com.example.e_commerceapp.databinding.FragmentOrdersBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ordersFragment : Fragment() {
    private lateinit var binding: FragmentOrdersBinding
    private val viewModel by viewModels<OrdersVirewModel>   ()
    private val orderAdapter by lazy{OrdersAdapter()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentOrdersBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpOrderRecyclerView()

        lifecycleScope.launch {
            viewModel.allOrders.collectLatest {
                when(it){
                    is Resourse.Error -> {
                        binding.progressbarAllOrders.visibility=View.INVISIBLE
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()

                    }
                    is Resourse.Loading -> {
                        binding.progressbarAllOrders.visibility=View.VISIBLE
                    }
                    is Resourse.Success -> {

                        binding.progressbarAllOrders.visibility=View.INVISIBLE
                        orderAdapter.differ.submitList(it.data)

                        if(it.data.isNullOrEmpty()){
                            binding.tvEmptyOrders.visibility=View.VISIBLE
                        }
                    }
                    is Resourse.Unspecified -> Unit
                }
            }

        }

        orderAdapter.onClick={
            val action=ordersFragmentDirections.actionOrdersFragmentToOrderDetailFragmentFragment(it)
            findNavController().navigate(action)
        }

    }

    private fun setUpOrderRecyclerView() {
            binding.rvAllOrders.apply {
               adapter= orderAdapter
                layoutManager=LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            }
    }
}