package com.example.e_commerceapp.Fragments.shopping

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.e_commerceapp.Activity.LoginRegisterActivity
import com.example.e_commerceapp.R
import com.example.e_commerceapp.Utils.Resourse
import com.example.e_commerceapp.Utils.showBottomNavitaion
import com.example.e_commerceapp.ViewModel.ProfileViewModel
import com.example.e_commerceapp.databinding.FragmentProfileBinding

import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.BuildConfig
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.constraintProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_fragment_user_Account)

        }


        binding.linearAllOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_ordersFragment)
        }
        binding.linearBilling.setOnClickListener {
            val action=ProfileFragmentDirections.actionProfileFragmentToBillingFragment(0f,
                emptyArray(),false)
            findNavController().navigate(action)
        }
        binding.logout.setOnClickListener {
            viewModel.logOut()
            val intent=Intent(requireActivity(),LoginRegisterActivity::class.java)
           startActivity(intent)
            requireActivity().finish()
        }

        binding.tvVersion.text = "Version ${BuildConfig.VERSION_CODE}"

        lifecycleScope.launch {
            viewModel.user.collectLatest {
                when(it){
                    is Resourse.Error -> {
                        binding.progressbarSettings.visibility=View.INVISIBLE
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()

                    }
                    is Resourse.Loading -> binding.progressbarSettings.visibility=View.VISIBLE
                    is Resourse.Success -> {
                        binding.progressbarSettings.visibility=View.INVISIBLE
                        Glide.with(requireView()).load(it.data!!.imagePath ).error(ColorDrawable(
                            Color.BLACK)).into(binding.imageUser)
                        binding.tvUserName.text=it.data.firstName+" "+it.data.lastName
                    }
                    is Resourse.Unspecified -> Unit
                }
            }
        }




    }

    override fun onResume() {
        super.onResume()
        showBottomNavitaion()
    }
}