package com.example.e_commerceapp.Utils

import android.view.View
import androidx.fragment.app.Fragment
import com.example.e_commerceapp.Activity.ShopingActivity
import com.example.e_commerceapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavitaion(){
    val bottomNavigationView=(activity as ShopingActivity).findViewById<BottomNavigationView>(
        R.id.bottomNavigation)
    bottomNavigationView.visibility=View.GONE
}

fun Fragment.showBottomNavitaion(){
    val bottomNavigationView=(activity as ShopingActivity).findViewById<BottomNavigationView>(
        R.id.bottomNavigation)
    bottomNavigationView.visibility=View.VISIBLE
}