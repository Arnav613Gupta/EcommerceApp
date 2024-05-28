package com.example.e_commerceapp.Dialog

import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.e_commerceapp.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.setUpBottomSheetDialog(
    onSendClick:(String)->Unit
){
    val dialog=BottomSheetDialog(requireContext())
    val view=layoutInflater.inflate(R.layout.reset_password_dialog,null)
    dialog.setContentView(view)
    dialog.behavior.state=BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val edtEmail=view.findViewById<EditText>(R.id.edtResetPasswordEmail)
    val btnSend=view.findViewById<Button>(R.id.btnSendResetPass)
    val btnCancel=view.findViewById<Button>(R.id.btnCancelResetPass)


    btnSend.setOnClickListener{
        val email=edtEmail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()

    }


    btnCancel.setOnClickListener{
        dialog.dismiss()
    }
}