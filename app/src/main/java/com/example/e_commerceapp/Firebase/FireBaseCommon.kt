package com.example.e_commerceapp.Firebase

import com.example.e_commerceapp.data.CartProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import javax.inject.Inject

class FireBaseCommon @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    private val cartCollection=firestore.collection("user").document(auth.uid!!).collection("cart")

    fun addToCart(cartProduct: CartProduct,onResult:(CartProduct?,Exception?)->Unit){
        cartCollection.document().set(cartProduct).addOnSuccessListener {
            onResult(cartProduct,null)
        }.addOnFailureListener {
            onResult(null,it)
        }

    }

    fun increaseQuantity(documentId:String,onResult: (String?, Exception?) -> Unit){
        firestore.runTransaction{transaction->
            val documentRef=cartCollection.document(documentId)
            val document=transaction.get(documentRef)
            val productObject=document.toObject(CartProduct::class.java)
            productObject?.let{cartProduct ->
                val newQuantity=cartProduct.quantity+1
                val newProductObject=cartProduct.copy(quantity = newQuantity)
                transaction.set(documentRef,newProductObject)

            }

        }.addOnFailureListener {
            onResult(null,it)
        }.addOnSuccessListener {
            onResult(documentId,null)
        }

    }


    fun decreaseQuantity(documentId:String,onResult: (String?, Exception?) -> Unit){
        firestore.runTransaction{transaction->
            val documentRef=cartCollection.document(documentId)
            val document=transaction.get(documentRef)
            val productObject=document.toObject(CartProduct::class.java)
            productObject?.let{cartProduct ->
                val newQuantity=cartProduct.quantity-1
                val newProductObject=cartProduct.copy(quantity = newQuantity)
                transaction.set(documentRef,newProductObject)

            }

        }.addOnFailureListener {
            onResult(null,it)
        }.addOnSuccessListener {
            onResult(documentId,null)
        }

    }






enum class quantityChanging{

    INCREASE,DECREASE

}
}