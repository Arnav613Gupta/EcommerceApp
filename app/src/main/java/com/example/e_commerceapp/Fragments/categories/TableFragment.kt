package com.example.e_commerceapp.Fragments.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.e_commerceapp.Utils.Resourse
import com.example.e_commerceapp.ViewModel.CategoryViewMOdel
import com.example.e_commerceapp.ViewModel.Factory.BaseCategoryViewModelFactory
import com.example.e_commerceapp.data.Category
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TableFragment : BaseCategoryFragment() {

    @Inject
    lateinit var firestore: FirebaseFirestore


    val viewModel by viewModels<CategoryViewMOdel> {
        BaseCategoryViewModelFactory(firestore, Category.Table)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.offerProduct.collectLatest {
                when(it){
                    is Resourse.Error -> {
                        Snackbar.make(requireView(),it.message.toString(), Snackbar.LENGTH_LONG).show()
                        hideOfferLoading()

                    }
                    is Resourse.Loading -> {
                        showOfferLoading()

                    }
                    is Resourse.Success -> {
                        offerProductrAdapter.differ.submitList(it.data)
                        hideOfferLoading()


                    }
                    else->{ Unit

                    }
                }

            }
        }


        lifecycleScope.launch {
            viewModel.bestProduct.collectLatest {
                when(it){
                    is Resourse.Error -> {
                        Snackbar.make(requireView(),it.message.toString(), Snackbar.LENGTH_LONG).show()
                        hideBestProductLoading()

                    }
                    is Resourse.Loading -> {
                        showBestProductLoading()

                    }
                    is Resourse.Success -> {
                        bestProductrAdapter.differ.submitList(it.data)
                        hideBestProductLoading()



                    }
                    else->{
                        Unit
                    }
                }

            }
        }
    }



}