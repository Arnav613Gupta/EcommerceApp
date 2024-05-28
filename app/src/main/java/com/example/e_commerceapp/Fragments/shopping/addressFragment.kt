package com.example.e_commerceapp.Fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.e_commerceapp.R
import com.example.e_commerceapp.Utils.Resourse
import com.example.e_commerceapp.ViewModel.AddressViewModel
import com.example.e_commerceapp.data.Address
import com.example.e_commerceapp.databinding.FragmentAddressBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class addressFragment : Fragment() {

    private lateinit var binding:FragmentAddressBinding
    private val viewModel by viewModels<AddressViewModel>()
    val args by navArgs<addressFragmentArgs>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.addNewAddress.collectLatest {
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
                        findNavController().navigateUp()


                    }
                    is Resourse.Unspecified -> Unit
                }
            }
        }
        lifecycleScope.launch {
            viewModel.error.collectLatest {
                Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAddressBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val address=args.address
        if(address==null){
            binding.buttonDelelte.visibility=View.GONE
        }else{
            binding.apply {

                edAddressTitle.setText(address.addressTitle)
                edFullName.setText(address.fullName)
                edStreet.setText(address.street)
                edPhone.setText(address.phoneNo)
                edCity.setText(address.city)
                edState.setText(address.state)




            }
        }

        binding.buttonSave.setOnClickListener {
            val  address=Address(binding.edAddressTitle.text.toString(),binding.edFullName.text.toString()
                ,binding.edStreet.text.toString()
                ,binding.edPhone.text.toString(),
                binding.edCity.text.toString(),
                binding.edState.text.toString())

            viewModel.addAddress(address)
        }
    }
}