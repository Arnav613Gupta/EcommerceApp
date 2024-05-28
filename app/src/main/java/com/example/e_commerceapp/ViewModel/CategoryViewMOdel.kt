package com.example.e_commerceapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapp.Utils.Resourse
import com.example.e_commerceapp.data.Category
import com.example.e_commerceapp.data.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewMOdel constructor(
    private val firestore: FirebaseFirestore,
    private val category: Category
):ViewModel() {

    private val _offerProduct=MutableStateFlow<Resourse<List<Product>>>(Resourse.Unspecified())
    val offerProduct=_offerProduct.asStateFlow()

    private val _beatProduct=MutableStateFlow<Resourse<List<Product>>>(Resourse.Unspecified())
    val bestProduct=_beatProduct.asStateFlow()

    init {

        fetchBestProducts()
        fetchOfferProducts()
    }


    fun fetchOfferProducts(){
        viewModelScope.launch {
            _offerProduct.emit(Resourse.Loading())
        }
        firestore.collection("Products").whereEqualTo("category",category.category)
            .whereNotEqualTo("offerPercentage",null).get()
            .addOnSuccessListener{
                val products=it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _offerProduct.emit(Resourse.Success(products))
                }


            }.addOnFailureListener {
                viewModelScope.launch {
                    _offerProduct.emit(Resourse.Error(it.message.toString()))
                }

            }
    }


    fun fetchBestProducts(){
        viewModelScope.launch {
            _beatProduct.emit(Resourse.Loading())
        }
        firestore.collection("Products").whereEqualTo("category",category.category)
            .whereEqualTo("offerPercentage",null).get()
            .addOnSuccessListener{
                val products=it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _beatProduct.emit(Resourse.Success(products))
                }


            }.addOnFailureListener {
                viewModelScope.launch {
                    _beatProduct.emit(Resourse.Error(it.message.toString()))
                }

            }
    }



}