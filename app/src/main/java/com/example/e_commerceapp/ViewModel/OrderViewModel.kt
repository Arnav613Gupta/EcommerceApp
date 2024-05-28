package com.example.e_commerceapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapp.Utils.Resourse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.e_commerceapp.data.order.Order

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) :ViewModel(){

    private val _order=MutableStateFlow<Resourse<Order>>(Resourse.Unspecified())
    val order=_order.asStateFlow()


    fun placeOrder(orderProduct: com.example.e_commerceapp.data.order.Order){
        viewModelScope.launch { _order.emit(Resourse.Loading()) }
        firestore.runBatch {batch->
            //Todo Add order in user collection
            //Todo Add order in order collection
            //Todo Delete Product from user cart collection


            firestore.collection("user").document(firebaseAuth.uid!!)
                .collection("orders").document().set(orderProduct)

            firestore.collection("orders").document().set(orderProduct)


            firestore.collection("user").document(firebaseAuth.uid!!)
                .collection("cart").get()
                .addOnSuccessListener {
                    it.documents.forEach{
                        it.reference.delete()
                    }
                }



        }.addOnSuccessListener {
            viewModelScope.launch { _order.emit(Resourse.Success(orderProduct)) }
        }.addOnFailureListener {
            viewModelScope.launch { _order.emit(Resourse.Error(it.message.toString())) }
        }



    }
}