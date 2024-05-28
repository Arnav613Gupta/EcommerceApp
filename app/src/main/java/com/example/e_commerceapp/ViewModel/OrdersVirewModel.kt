package com.example.e_commerceapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapp.Utils.Resourse
import com.example.e_commerceapp.data.order.Order
import com.example.e_commerceapp.data.order.OrderStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersVirewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
):ViewModel() {

    private val _allOrders= MutableStateFlow<Resourse<List<Order>>>(Resourse.Unspecified())
    val allOrders=_allOrders.asStateFlow()

    init {
        getAllOrders()
    }

    fun getAllOrders(){
        viewModelScope.launch { _allOrders.emit(Resourse.Loading()) }

        firestore.collection("user").document(auth.uid!!).collection("orders")
            .get()
            .addOnSuccessListener {

                val orders=it.toObjects(Order::class.java)
                viewModelScope.launch {
                    _allOrders.emit(Resourse.Success(orders))
                }
            }.addOnFailureListener {

                viewModelScope.launch {
                    _allOrders.emit(Resourse.Error(it.message.toString()))
                }

            }
    }
}