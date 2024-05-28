package com.example.e_commerceapp.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapp.Utils.Resourse
import com.example.e_commerceapp.data.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
):ViewModel() {

    private val _specialProduct=MutableStateFlow<Resourse<List<Product>>>(Resourse.Unspecified())
    val specialProduct:StateFlow<Resourse<List<Product>>> = _specialProduct

    private val _bestProducts=MutableStateFlow<Resourse<List<Product>>>(Resourse.Unspecified())
    val bestProducts:StateFlow<Resourse<List<Product>>> = _bestProducts

    private val _bestDeals=MutableStateFlow<Resourse<List<Product>>>(Resourse.Unspecified())
    val bestDeals:StateFlow<Resourse<List<Product>>> = _bestDeals

    private val pagingInfo=PagingInfo()

    init {
        fetchSpecialProduct()
        fetchBestProduct()
        fetchBestDeals()
    }

     fun fetchBestDeals() {
         viewModelScope.launch {
             _bestDeals.emit(Resourse.Loading())
         }

         firestore.collection("Products").whereEqualTo("category","Best Deals").get()
             .addOnSuccessListener{result->
                 viewModelScope.launch {

                     val bestDealsList=result.toObjects(Product::class.java)

                     _bestDeals.emit(Resourse.Success(bestDealsList))
                 }
             }

             .addOnFailureListener {

                 viewModelScope.launch {
                     _bestDeals.emit(Resourse.Error(it.message.toString()))
                 }
             }
    }

     fun fetchBestProduct() {
         if (!pagingInfo.isPagingEnd) {
             viewModelScope.launch {
                 _bestProducts.emit(Resourse.Loading())
             }

             firestore.collection("Products").limit(pagingInfo.bestProductPage * 3).get()
                 .addOnSuccessListener { result ->

                     val bestproductList = result.toObjects(Product::class.java)

                     pagingInfo.isPagingEnd = bestproductList == pagingInfo.oldBsetProduct
                     pagingInfo.oldBsetProduct = bestproductList

                     viewModelScope.launch {

                         _bestProducts.emit(Resourse.Success(bestproductList))
                     }
                     pagingInfo.bestProductPage++
                 }

                 .addOnFailureListener {

                     viewModelScope.launch {
                         _bestProducts.emit(Resourse.Error(it.message.toString()))
                     }
                 }
         }
     }


    fun fetchSpecialProduct(){
        viewModelScope.launch {
            _specialProduct.emit(Resourse.Loading())
        }

        firestore.collection("Products").whereEqualTo("category","Special Product").get()
            .addOnSuccessListener{result->
                viewModelScope.launch {

                    val productList=result.toObjects(Product::class.java)

                    _specialProduct.emit(Resourse.Success(productList))
                }
            }

            .addOnFailureListener {

                viewModelScope.launch {
                    _specialProduct.emit(Resourse.Error(it.message.toString()))
                }
            }
    }



}

internal data class PagingInfo(
    var bestProductPage:Long=1,
    var oldBsetProduct:List<Product> = emptyList(),
    var isPagingEnd:Boolean=false
)