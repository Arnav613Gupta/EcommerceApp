package com.example.e_commerceapp.Fragments.LoginRegister

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerceapp.Activity.ShopingActivity
import com.example.e_commerceapp.Dialog.setUpBottomSheetDialog
import com.example.e_commerceapp.R
import com.example.e_commerceapp.Utils.Resourse
import com.example.e_commerceapp.ViewModel.LoginViewModel
import com.example.e_commerceapp.databinding.FragmentLogInBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogInFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentLogInBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.texRegisterHere.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_registerFragment)
        }


        binding.txtforgretPass.setOnClickListener {
            setUpBottomSheetDialog {email->
                viewModel.resetPassword(email)

            }
        }
        lifecycleScope.launch {

            viewModel.resetPassword.collect{

                when(it){
                    is Resourse.Error -> {
                        Snackbar.make(requireView(),"Error ${it.message}",Snackbar.LENGTH_LONG).show()

                    }
                    is Resourse.Loading -> {


                    }
                    is Resourse.Success -> {
                        Snackbar.make(requireView(),"Reset link sent to your account",Snackbar.LENGTH_LONG).show()


                    }
                    else -> Unit
                }

            }
        }

        binding.apply {

            btnLogIn.setOnClickListener {
                val email= edtEmailLogIn.text.toString().trim()
                val password=edtPasswordLogIn.text.toString().trim()

                viewModel.login(email,password)
            }

        }

        lifecycleScope.launch {
            viewModel.login.collect{
                when(it){
                    is Resourse.Error -> {
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                        binding.btnLogIn.revertAnimation()
                    }
                    is Resourse.Loading -> {
                        binding.btnLogIn.startAnimation()

                    }
                    is Resourse.Success -> {
                        Intent(requireActivity(),ShopingActivity::class.java).also {intent->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }


                    }
                    else -> Unit
                }
            }
        }


    }


}