package com.example.e_commerceapp.Utils

import android.util.Patterns


fun validateEmail(email:String):RegisterValidation{
    if(email.isEmpty()){
        return RegisterValidation.Failed("Email can not be Empty")
    }
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        return RegisterValidation.Failed("Wrong Email format")
    }
    return RegisterValidation.Success
}

fun validatePassword(password:String):RegisterValidation{
    if(password.trim().isEmpty()){
        return RegisterValidation.Failed("Password con not be Empty")
    }

    if (password.trim().length<6) {
        return RegisterValidation.Failed("Password should contain 6 char")
    }
    return RegisterValidation.Success
}