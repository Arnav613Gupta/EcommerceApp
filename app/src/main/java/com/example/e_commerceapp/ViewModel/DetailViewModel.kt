package com.example.e_commerceapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapp.Firebase.FireBaseCommon
import com.example.e_commerceapp.Utils.Resourse
import com.example.e_commerceapp.data.CartProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private   val firestore: FirebaseFirestore,
    val auth: FirebaseAuth,
    private val fireBaseCommon: FireBaseCommon
):ViewModel() {

    private val _addToCart=MutableStateFlow<Resourse<CartProduct>>(Resourse.Unspecified())
    val addToCart=_addToCart.asStateFlow()


    fun addUpdateProductInCart(cardProduct: CartProduct){
        viewModelScope.launch { _addToCart.emit(Resourse.Loading()) }
        firestore.collection("user").document(auth.uid!!).collection("cart").whereEqualTo("product.id",cardProduct.product.id).get()
            .addOnSuccessListener {
                it.documents.let{
                    if(it.isEmpty()){ //add new Product
                        addProduct(cardProduct)

                    }else{
                        val product=it.first().toObject(CartProduct::class.java)
                        if(product==cardProduct){
                            val documentId=it.first().id
                            increaseQuantity(cardProduct,documentId)

                        }else{//add new Product
                            addProduct(cardProduct)
                        }
                    }
                }
            }.addOnFailureListener {
                viewModelScope.launch { _addToCart.emit(Resourse.Error(it.message.toString())) }

            }

    }

    private fun addProduct(cartProduct: CartProduct){
        fireBaseCommon.addToCart(cartProduct){addedProduct,e ->
            viewModelScope.launch {
                if(e==null){
                    _addToCart.emit(Resourse.Success(addedProduct!!))
                }else{
                    _addToCart.emit(Resourse.Error(e.message.toString()))
                }

                 }


        }
    }

    private fun increaseQuantity(cartProduct: CartProduct,id:String){
        fireBaseCommon.increaseQuantity(id){addedId,e ->
            viewModelScope.launch {
                if(e==null){
                    _addToCart.emit(Resourse.Success(cartProduct!!))
                }else{
                    _addToCart.emit(Resourse.Error(e.message.toString()))
                }

            }


        }
    }

}