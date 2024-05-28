package com.example.e_commerceapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapp.Utils.Resourse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) :ViewModel() {

    private val _login= MutableSharedFlow<Resourse<FirebaseUser>>()
    val login=_login.asSharedFlow()

    private val _resetPassword= MutableSharedFlow<Resourse<String>>()
     val resetPassword= _resetPassword.asSharedFlow()


    fun login(email:String,password:String){
        viewModelScope.launch {
            _login.emit(Resourse.Loading())
        }

        firebaseAuth.signInWithEmailAndPassword(email,password).
        addOnSuccessListener {
            viewModelScope.launch {
                it.user?.let {
                    _login.emit(Resourse.Success(it))
                }
            }

        }.addOnFailureListener{
            viewModelScope.launch {
                _login.emit(Resourse.Error(it.message.toString()))
            }

        }

    }


    fun resetPassword(email:String){
        viewModelScope.launch {
            _resetPassword.emit(Resourse.Loading())
        }
            firebaseAuth
                .sendPasswordResetEmail(email)
                .addOnSuccessListener{
                    viewModelScope.launch {
                        _resetPassword.emit(Resourse.Success(email))
                    }

                }
                .addOnFailureListener {

                    viewModelScope.launch {
                        _resetPassword.emit(Resourse.Error(it.message.toString()))
                    }
                }


    }
}