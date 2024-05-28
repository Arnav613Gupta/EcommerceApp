package com.example.e_commerceapp.ViewModel

import androidx.lifecycle.ViewModel
import com.example.e_commerceapp.Utils.Constants.USER_COLLECTION
import com.example.e_commerceapp.Utils.RegisterFieldStatus
import com.example.e_commerceapp.Utils.RegisterValidation
import com.example.e_commerceapp.Utils.Resourse
import com.example.e_commerceapp.Utils.validateEmail
import com.example.e_commerceapp.Utils.validatePassword
import com.example.e_commerceapp.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db:FirebaseFirestore
) : ViewModel() {


    private val _register= MutableStateFlow<Resourse<User>>(Resourse.Unspecified())
    val register:Flow<Resourse<User>> = _register


    private val _validation= Channel<RegisterFieldStatus>()
    val validation=_validation.receiveAsFlow()

    fun createAccountWithEmailPassword(user: User,password:String){

        if(checkValidation(user, password)) {
            runBlocking {
                _register.emit(Resourse.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        saveUserInfo(it.uid,user)

                    }

                }.addOnFailureListener {
                    _register.value = Resourse.Error(it.message.toString())

                }
        }else{
            val registerFieldStatus=RegisterFieldStatus(validateEmail(user.email), validatePassword(password))

            runBlocking {
                _validation.send(registerFieldStatus)
            }


        }
    }



    private fun checkValidation(user: User, password: String) :Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val shouldRegister =
            emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success

        return shouldRegister
    }

    private fun saveUserInfo(userUid: String,user:User) {
        db.collection(USER_COLLECTION).
        document(userUid).
        set(user).
        addOnSuccessListener {
            _register.value = Resourse.Success(user)
        }.
        addOnFailureListener {
            _register.value = Resourse.Error(it.message.toString())

        }

    }



}