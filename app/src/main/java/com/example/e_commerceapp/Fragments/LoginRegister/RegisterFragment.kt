package com.example.e_commerceapp.Fragments.LoginRegister

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerceapp.R
import com.example.e_commerceapp.Utils.RegisterValidation
import com.example.e_commerceapp.Utils.Resourse
import com.example.e_commerceapp.ViewModel.RegisterViewModel
import com.example.e_commerceapp.data.User
import com.example.e_commerceapp.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding:FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.txtLogInHere.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_logInFragment)
        }

        binding.apply {
            btnRegisterAccount.setOnClickListener{

                    val user = User(
                        edtFirstNameRegister.text.toString().trim(),
                        edtLastNameRegister.text.toString().trim(),
                        edtEmailRegister.text.toString().trim()
                    )

                    val password = edtPasswordRegister.text.toString()

                    viewModel.createAccountWithEmailPassword(user, password)



            }

        }

        lifecycleScope.launch {
            viewModel.register.collect(){
                when(it){
                    is Resourse.Loading -> {
                        binding.btnRegisterAccount.startAnimation()
                    }
                    is Resourse.Success ->{
                        Log.d("Tag-",it.data.toString())
                        binding.btnRegisterAccount.revertAnimation()
                    }
                    is Resourse.Error ->{
                        Log.e("Tag-",it.message.toString())
                        binding.btnRegisterAccount.revertAnimation()
                    }
                    else -> Unit

                }
            }

        }

        lifecycleScope.launch {
            viewModel.validation.collect {validation ->
                if(validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){

                        binding.edtEmailRegister.apply {
                            requestFocus()
                            error=validation.email.message
                        }

                    }
                }

                if(validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){

                        binding.edtPasswordRegister.apply {
                            requestFocus()
                            error=validation.password.message
                        }

                    }
                }

            }

        }

    }


}


