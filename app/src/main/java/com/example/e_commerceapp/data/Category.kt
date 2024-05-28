package com.example.e_commerceapp.data

sealed class Category(val category:String) {


    object Chair:Category("Chair")
    object Table:Category("Table")
    object Cubboard:Category("Cupboard")
    object Furniture:Category("Furniture")
    object Accessory:Category("Accessory")
}