package com.example.mochat.modal

import android.content.Context
import android.widget.Toast
import com.example.mochat.domain.UserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ModalDatabase {
    var maAuth: FirebaseAuth? = null
    var currentUser: FirebaseUser? = null
    var fireBaseDatabaseRef:DatabaseReference?=null
    var mContext:Context?=null
    var messageTobeDisplay:String?=null
    constructor(mContext:Context) {
        this.mContext=mContext
        maAuth = FirebaseAuth.getInstance()
        currentUser = maAuth?.currentUser
        fireBaseDatabaseRef= FirebaseDatabase.getInstance().reference
    }
    constructor(mContext:Context,message:String) {
        this.mContext=mContext
        this.messageTobeDisplay=message
        maAuth = FirebaseAuth.getInstance()
        currentUser = maAuth?.currentUser
        fireBaseDatabaseRef= FirebaseDatabase.getInstance().reference
    }

    fun setDataForAParticularUser(user:UserDto) {
            fireBaseDatabaseRef?.child("Users")?.child(currentUser?.uid!!)?.setValue(user)?.addOnCompleteListener({
                if(it.isSuccessful)
                {
                    if(messageTobeDisplay!=null)
                    {
                        Toast.makeText(mContext,messageTobeDisplay,Toast.LENGTH_LONG).show()
                    }
                }
                else
                {
                    Toast.makeText(mContext,"Something Went Wrong  ",Toast.LENGTH_LONG).show()
                }
            })
    }

    fun getDataOfAParticularUser():Any
    {   var user:Any?=null
        fireBaseDatabaseRef?.child("Users")?.child(currentUser?.uid!!)?.get()?.addOnSuccessListener ({
            user=it.value
        })?.addOnFailureListener({
            Toast.makeText(mContext,"Error getting data",Toast.LENGTH_LONG)
        })
        return user!!
    }
}