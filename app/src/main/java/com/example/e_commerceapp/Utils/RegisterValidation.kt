package com.example.e_commerceapp.Utils

sealed class RegisterValidation(){
    object Success: RegisterValidation()
    data class Failed(val message:String):RegisterValidation()

}
data class RegisterFieldStatus(
    val email:RegisterValidation,
    val password:RegisterValidation
)