package com.example.e_commerceapp.Helper

fun Float?.getProductPrice(price :Float):Float{
    // this -> percentage
    if(this == null){
        return price
    }
    val remainperce=1f-this
    val newPrice=price * remainperce
    return newPrice
}