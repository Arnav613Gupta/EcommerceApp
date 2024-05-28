package com.example.e_commerceapp.Fragments.LoginRegister

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerceapp.Activity.ShopingActivity
import com.example.e_commerceapp.R
import com.example.e_commerceapp.ViewModel.IntroViewModel
import com.example.e_commerceapp.ViewModel.IntroViewModel.Companion.ACCOUNT_OPTION_FRAGMENT
import com.example.e_commerceapp.ViewModel.IntroViewModel.Companion.SHOPPING_ACTIVITY
import com.example.e_commerceapp.databinding.FragmentIntroBinding
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroFragment : Fragment() {
    private lateinit var binding: FragmentIntroBinding

    private val viewModel by viewModels<IntroViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentIntroBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launch{
            viewModel.navigate.collect {
                when(it){
                    SHOPPING_ACTIVITY ->{

                        Intent(requireActivity(), ShopingActivity::class.java).also { intent->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    ACCOUNT_OPTION_FRAGMENT ->{
                        findNavController().navigate(R.id.action_introFragment_to_accountOptionFragment)

                    }else-> Unit



                }
            }
        }


        binding.btnStart.setOnClickListener {
            viewModel.startButtonClick()
            findNavController().navigate(R.id.action_introFragment_to_accountOptionFragment)
        }
    }


}