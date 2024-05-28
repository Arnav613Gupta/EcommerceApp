package com.example.e_commerceapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapp.Utils.Resourse
import com.example.e_commerceapp.data.Address
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewMOdel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth

) :ViewModel() {

    private val _address=MutableStateFlow<Resourse<List<Address>>>(Resourse.Unspecified())
    val address=_address.asStateFlow()

init {
    getUserAddress()
}
    fun getUserAddress(){
        viewModelScope.launch { _address.emit(Resourse.Loading()) }
        firestore.collection("user").document(auth.uid!!).collection("address")
            .addSnapshotListener { value, error ->
                if(error!=null){
                    viewModelScope.launch { _address.emit(Resourse.Error(error.message.toString())) }
                    return@addSnapshotListener
                }
                val addresses= value?.toObjects(Address::class.java)
                viewModelScope.launch { _address.emit(Resourse.Success(addresses!!)) }
            }
    }
}