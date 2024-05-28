package com.example.e_commerceapp.ViewModel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapp.E_Shop
import com.example.e_commerceapp.Utils.RegisterValidation
import com.example.e_commerceapp.Utils.Resourse
import com.example.e_commerceapp.Utils.validateEmail
import com.example.e_commerceapp.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage:StorageReference,
     app:Application
):AndroidViewModel(app) {

    private val _user=MutableStateFlow<Resourse<User>>(Resourse.Unspecified())
    val user=_user.asStateFlow()


    private val _updateInfo=MutableStateFlow<Resourse<User>>(Resourse.Unspecified())
    val editInfo=_updateInfo.asStateFlow()

    init {
        getUser()
    }

    fun getUser(){
        viewModelScope.launch { _user.emit(Resourse.Loading()) }

        firestore.collection("user").document(auth.uid!!).get()
            .addOnSuccessListener {
                val user=it.toObject(User::class.java)
                user?.let {
                    viewModelScope.launch { _user.emit(Resourse.Success(user)) }
                }

            }.addOnFailureListener {
                viewModelScope.launch { _user.emit(Resourse.Error(it.message.toString())) }
            }
    }


    fun updateUser( user:User,imageUri: Uri?){
        val areInputsValid= validateEmail(user.email) is RegisterValidation.Success &&
                user.firstName.trim().isNotEmpty() &&
                user.lastName.trim().isNotEmpty()

        if(!areInputsValid){
            viewModelScope.launch { _user.emit(Resourse.Error("Check your inputs")) }
            return
        }


        viewModelScope.launch { _updateInfo.emit(Resourse.Loading()) }

        if (imageUri ==null){
            saveUserInfo(user,true)
        }else{
            saveUserInfoWithNewImage(user,imageUri)
        }

    }

    private fun saveUserInfoWithNewImage(user: User, imageUri: Uri?) {
        viewModelScope.launch {
            try {

                val imageBitmap = MediaStore.Images.Media.getBitmap(
                    getApplication<E_Shop>().contentResolver,
                    imageUri
                )
                val byteArrayOutputStream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 96, byteArrayOutputStream)
                val imageByteArray = byteArrayOutputStream.toByteArray()



                val imageDirectory=storage.child("profileImages/${auth.uid}/${UUID.randomUUID()}")

                val result=imageDirectory.putBytes(imageByteArray).await()
                val imageUri=result.storage.downloadUrl.await().toString()
                saveUserInfo(user.copy(imagePath = imageUri),false)


            }catch (e:Exception){
                viewModelScope.launch { _updateInfo.emit(Resourse.Error(e.message.toString())) }

            }
        }
    }

    private fun saveUserInfo(user: User, shouldRetriveOldImage: Boolean) {

        firestore.runTransaction{transaction->
            val documentRef=firestore.collection("user").document(auth.uid!!)
            if (shouldRetriveOldImage){
                val currentUser=transaction.get(documentRef).toObject(User::class.java)
                val newUser=user.copy(imagePath = currentUser?.imagePath ?: "")
                transaction.set(documentRef,newUser)
            }else{
                transaction.set(documentRef,user)
            }

        }.addOnSuccessListener {
            viewModelScope.launch { _updateInfo.emit(Resourse.Success(user)) }

        }.addOnFailureListener {
            viewModelScope.launch { _updateInfo.emit(Resourse.Error(it.message.toString())) }

        }

    }


}