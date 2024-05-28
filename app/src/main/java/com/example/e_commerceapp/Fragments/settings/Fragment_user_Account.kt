package com.example.e_commerceapp.Fragments.settings

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.e_commerceapp.Dialog.setUpBottomSheetDialog
import com.example.e_commerceapp.R
import com.example.e_commerceapp.Utils.Resourse
import com.example.e_commerceapp.ViewModel.UserAccountViewModel
import com.example.e_commerceapp.data.User
import com.example.e_commerceapp.databinding.FragmentUserAccountBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Fragment_user_Account : Fragment() {

    private lateinit var  binding:FragmentUserAccountBinding
    private lateinit var imageActivityLauncher:ActivityResultLauncher<Intent>
    private val viewModel by viewModels<UserAccountViewModel> ()
    private var imageUri : Uri? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageActivityLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            imageUri=it.data?.data
            Glide.with(this).load(imageUri).into(binding.imageUser)
        }


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentUserAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        lifecycleScope.launch {
            viewModel.user.collectLatest {
                when(it){
                    is Resourse.Error -> {
                        hideUserLoading()

                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resourse.Loading -> {
                        showUserLoading()



                    }
                    is Resourse.Success -> {


                        hideUserLoading()
                        showUserInfo(it.data!!)
                    }
                    else -> Unit
                }
            }
        }



        lifecycleScope.launch {
            viewModel.editInfo.collectLatest {
                when(it){
                    is Resourse.Error -> {


                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resourse.Loading -> {
                        binding.buttonSave.startAnimation()

                    }
                    is Resourse.Success -> {
                        binding.buttonSave.revertAnimation()
                        findNavController().navigateUp()
                    }
                    else -> Unit
                }
            }
        }


        binding.tvUpdatePassword.setOnClickListener {
            setUpBottomSheetDialog {  }
        }



        binding.buttonSave.setOnClickListener {
            val user=User(binding.edFirstName.text.toString(),binding.edLastName.text.toString(),
                binding.edEmail.text.toString())

            viewModel.updateUser(user,imageUri)
        }


        binding.imageEdit.setOnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type="image/*"
            imageActivityLauncher.launch(intent)

        }



    }

    private fun showUserInfo(data: User) {
        binding.apply {
            Glide.with(this@Fragment_user_Account).load(data.imagePath).error(ColorDrawable(Color.BLACK)).into(imageUser)
            edFirstName.setText(data.firstName)
            edLastName.setText(data.lastName)
            edEmail.setText(data.email)
        }


    }

    private fun hideUserLoading() {
        binding.apply {
            progressbarAccount.visibility=View.INVISIBLE

            imageUser.visibility=       View.VISIBLE
            imageEdit.visibility=       View.VISIBLE
            edFirstName.visibility=     View.VISIBLE
            edLastName.visibility=      View.VISIBLE
            tvUpdatePassword.visibility=View.VISIBLE
            edEmail.visibility=         View.VISIBLE
            buttonSave.visibility=      View.VISIBLE
        }
    }

    private fun showUserLoading() {
        binding.apply {
           progressbarAccount.visibility=View.VISIBLE
            imageUser.visibility=       View.INVISIBLE
            imageEdit.visibility=       View.INVISIBLE
            edFirstName.visibility=     View.INVISIBLE
            edLastName.visibility=      View.INVISIBLE
            tvUpdatePassword.visibility=View.INVISIBLE
            edEmail.visibility=         View.INVISIBLE
            buttonSave.visibility=      View.INVISIBLE



        }
    }
}