package com.example.e_commerceapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapp.Utils.Resourse
import com.example.e_commerceapp.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
):ViewModel() {

    private val _user= MutableStateFlow<Resourse<User>>(Resourse.Unspecified())
    val user=_user.asStateFlow()


    init {
        getUser()
    }
    fun getUser(){
        viewModelScope.launch { _user.emit(Resourse.Loading()) }

        firestore.collection("user").document(firebaseAuth.uid!!)
            .addSnapshotListener { value, error ->
                if(error!=null){
                    viewModelScope.launch { _user.emit(Resourse.Error(error.message.toString())) }


                }else{
                    val user=value?.toObject(User::class.java)
                    user?.let {
                        viewModelScope.launch { _user.emit(Resourse.Success(user)) }
                    }



                }
            }
    }

    fun logOut(){
        firebaseAuth.signOut()
    }

}