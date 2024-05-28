package com.example.e_commerceapp.data.order

import android.os.Parcelable
import com.example.e_commerceapp.data.Address
import com.example.e_commerceapp.data.CartProduct
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.random.Random

@Parcelize
data class Order(
     val orderStatus:String="",
    val price:Float=0f,
    val product: List<CartProduct>  = emptyList(),
    val address: Address=Address(),
    val date:String=SimpleDateFormat("yyyy-mm-dd",Locale.ENGLISH).format(Date()),
    val orderId:Long= Random.nextLong(0,100_000_000_000)+price.toLong()

):Parcelable