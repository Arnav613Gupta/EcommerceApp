package com.example.e_commerceapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.e_commerceapp.R
import com.example.e_commerceapp.Utils.Resourse
import com.example.e_commerceapp.ViewModel.CartViewModel
import com.example.e_commerceapp.databinding.ActivityShopingBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ShopingActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityShopingBinding.inflate(layoutInflater)
    }
    val viewModel by viewModels<CartViewModel> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val navController =findNavController(R.id.shoppinghostfragment)
        binding.bottomNavigation.setupWithNavController(navController)

        lifecycleScope.launch {
            viewModel.cartProducts.collectLatest {
                when(it){

                    is Resourse.Success -> {

                        val count=it.data?.size ?:0
                        val bottomNavigation=findViewById<BottomNavigationView>(R.id.bottomNavigation)
                        bottomNavigation.getOrCreateBadge(R.id.cartFragment).apply {
                            number =count
                            backgroundColor=resources.getColor(R.color.g_blue)
                        }
                    }
                    else -> {
                        Unit
                    }
                }
            }
        }
    }
}