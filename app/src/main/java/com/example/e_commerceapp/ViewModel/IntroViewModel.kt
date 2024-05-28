package com.example.e_commerceapp.ViewModel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapp.R
import com.example.e_commerceapp.Utils.Constants.INTRODUCTIOn_Key
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val firebaseAuth: FirebaseAuth
) :ViewModel(){

    private val _navigate= MutableStateFlow(0)
    val navigate:StateFlow<Int> = _navigate


    companion object{
        const val SHOPPING_ACTIVITY = 23
         val ACCOUNT_OPTION_FRAGMENT= R.id.action_introFragment_to_accountOptionFragment
    }

    init {
        val isButtonClicked=sharedPreferences.getBoolean(INTRODUCTIOn_Key,false)
        val user=firebaseAuth.currentUser

        if (user !=null){
            viewModelScope.launch {
                _navigate.emit(SHOPPING_ACTIVITY)
            }
        }
        else if(isButtonClicked){
            viewModelScope.launch {
                _navigate.emit(ACCOUNT_OPTION_FRAGMENT)
            }
        }
        else{
            Unit
        }
    }

    fun startButtonClick(){
        sharedPreferences.edit().putBoolean(INTRODUCTIOn_Key,true).apply()
    }
}