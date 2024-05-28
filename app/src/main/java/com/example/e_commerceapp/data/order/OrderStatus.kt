package com.example.e_commerceapp.data.order

sealed class OrderStatus(val status: String){

    object Ordered:OrderStatus("Ordered")
    object Canceled:OrderStatus("Canceled")
    object Confirmed:OrderStatus("Confirmed")
    object Shiped:OrderStatus("Shiped")
    object Delivered:OrderStatus("Delivered")
    object Returned:OrderStatus("Returned")

}

fun getOrderedStatus(status:String):OrderStatus{
    return when (status){
        "Ordered"->{
            OrderStatus.Ordered
        }
        "Canceled" -> {
            OrderStatus.Canceled
        }
        "Confirmed" ->{
            OrderStatus.Confirmed
        }
        "Shiped" ->{
            OrderStatus.Shiped
        }
        "Delivered"->{
            OrderStatus.Delivered
        }
        else->{
            OrderStatus.Returned
        }




    }

}