package com.example.e_commerceapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapp.Firebase.FireBaseCommon

import com.example.e_commerceapp.Helper.getProductPrice
import com.example.e_commerceapp.Utils.Resourse
import com.example.e_commerceapp.data.CartProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val  fireBaseCommon: FireBaseCommon
    ):ViewModel() {

    private val _cartProduct =MutableStateFlow<Resourse<List<CartProduct>>>(Resourse.Unspecified())
     val cartProducts =_cartProduct.asStateFlow()

    private val _deleteDialog= MutableSharedFlow<CartProduct>()
    val deleteDialog=_deleteDialog.asSharedFlow()

    val productPrice =cartProducts.map {
        when(it){
            is Resourse.Success->{
                calculatePrice(it.data!!)
            }
            else -> null
        }
    }

    private fun calculatePrice(data: List<CartProduct>): Float {
        return data.sumByDouble { cartProduct ->
            (cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price)*cartProduct.quantity).toDouble()

        }.toFloat()

    }

    private var cartProductDocument = emptyList<DocumentSnapshot>()
    init {
        getCartProducts()
    }

    private fun getCartProducts() {
        viewModelScope.launch {
            _cartProduct.emit(Resourse.Loading())
        }

        firestore.collection("user").document(auth.uid!!).collection("cart")
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {

                    viewModelScope.launch {
                        _cartProduct.emit(Resourse.Error(error?.message.toString()))
                    }
                } else {
                    cartProductDocument = value.documents
                    val cartProduct = value.toObjects(CartProduct::class.java)
                    viewModelScope.launch { _cartProduct.emit(Resourse.Success(cartProduct)) }
                }
            }
    }

        fun increaseQuantity(documentId: String) {
            fireBaseCommon.increaseQuantity(documentId) { result, e ->
                if (e != null) {
                    viewModelScope.launch { _cartProduct.emit(Resourse.Error(e.message.toString())) }
                }

            }
        }

            fun decreaseQuantity(documentId: String) {
                fireBaseCommon.decreaseQuantity(documentId) { result, e ->
                    if (e != null) {
                        viewModelScope.launch { _cartProduct.emit(Resourse.Error(e.message.toString())) }
                    }

                }
            }

            fun deleteCartProduct(cartProduct: CartProduct) {
                val index = cartProducts.value.data?.indexOf(cartProduct)
                if (index != null && index != -1) {
                    val documentId = cartProductDocument[index].id
                    firestore.collection("user").document(auth.uid!!).collection("cart").document(documentId).delete()

                }
            }

            fun changeQuantity(cartProduct: CartProduct, quantityChanging: FireBaseCommon.quantityChanging) {

                val index = cartProducts.value.data?.indexOf(cartProduct)
                if (index != null && index != -1) {
                    val documentId = cartProductDocument[index].id
                    when (quantityChanging) {
                        FireBaseCommon.quantityChanging.INCREASE -> {
                            viewModelScope.launch { _cartProduct.emit(Resourse.Loading()) }
                            increaseQuantity(documentId)

                        }

                        FireBaseCommon.quantityChanging.DECREASE  -> {

                            if(cartProduct.quantity==1){
                             viewModelScope.launch {  _deleteDialog.emit(cartProduct) }
                                return
                            }

                            viewModelScope.launch { _cartProduct.emit(Resourse.Loading()) }
                            decreaseQuantity(documentId)
                        }
                    }
                }

            }


    }