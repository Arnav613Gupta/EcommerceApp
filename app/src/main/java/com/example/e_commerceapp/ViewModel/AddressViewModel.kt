package com.example.e_commerceapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapp.Utils.Resourse
import com.example.e_commerceapp.data.Address
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
):ViewModel() {

    private val _addNewAddress=MutableStateFlow<Resourse<Address>>(Resourse.Unspecified())
     val addNewAddress=_addNewAddress.asStateFlow()


    private val _error= MutableSharedFlow<String>()
    val error=_error.asSharedFlow()


    fun addAddress(address: Address){
        val areInputsCorrect=validateInputs(address)
        if (areInputsCorrect) {
            viewModelScope.launch { _addNewAddress.emit(Resourse.Loading()) }
            firestore.collection("user").document(auth.uid!!).collection("address").document()
                .set(address)
                .addOnSuccessListener {
                    viewModelScope.launch { _addNewAddress.emit(Resourse.Success(address)) }
                }.addOnFailureListener {

                    viewModelScope.launch { _addNewAddress.emit(Resourse.Error(it.message.toString())) }
                }
        }else{
            viewModelScope.launch {
                _error.emit("All fields are required")
            }
        }

    }

    private fun validateInputs(address: Address): Boolean {
        return address.addressTitle.trim().isNotEmpty() && address.city.trim().isNotEmpty() &&
                address.phoneNo.trim().isNotEmpty() && address.state.trim().isNotEmpty() &&
                address.street.trim().isNotEmpty() &&address.fullName.trim().isNotEmpty()

    }
}