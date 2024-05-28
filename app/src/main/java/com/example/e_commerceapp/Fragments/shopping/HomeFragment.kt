package com.example.e_commerceapp.Fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_commerceapp.Adapters.HomeViewPagerAdapter
import com.example.e_commerceapp.Fragments.categories.AccessoryFragment
import com.example.e_commerceapp.Fragments.categories.ChairFragment
import com.example.e_commerceapp.Fragments.categories.CupboardFragment
import com.example.e_commerceapp.Fragments.categories.FurnitureFragment
import com.example.e_commerceapp.Fragments.categories.MainFragment
import com.example.e_commerceapp.Fragments.categories.TableFragment
import com.example.e_commerceapp.R
import com.example.e_commerceapp.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryFragments= arrayListOf<Fragment>(

            MainFragment(),ChairFragment(),CupboardFragment(),
            TableFragment(),AccessoryFragment(),FurnitureFragment()
        )

        binding.viewPagerHome.isUserInputEnabled=false


        val viewPager2Adapter=HomeViewPagerAdapter(categoryFragments,childFragmentManager,lifecycle)
        binding.viewPagerHome.adapter=viewPager2Adapter
        TabLayoutMediator(binding.tabLayout,binding.viewPagerHome){tab,position->
            when(position){
                0-> tab.text = "Main"
                1-> tab.text = "Chair"
                2-> tab.text = "CupBoard"
                3-> tab.text = "Table"
                4-> tab.text = "Accessories"
                5-> tab.text = "Furniture"
            }

        }.attach()
    }



}