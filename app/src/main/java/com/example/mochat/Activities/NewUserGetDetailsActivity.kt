package com.example.mochat.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mochat.R
import com.example.mochat.domain.UserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NewUserGetDetailsActivity:AppCompatActivity() {
    var maAuth: FirebaseAuth? = null
    var currentUser: FirebaseUser? = null
    var fireBaseDatabaseRef: DatabaseReference?=null
    var progressBar: ProgressBar?=null
    var editText:EditText?=null
    var continieButton:Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user_get_details)
        maAuth = FirebaseAuth.getInstance()
        currentUser = maAuth?.currentUser
        fireBaseDatabaseRef= FirebaseDatabase.getInstance().reference
        progressBar= ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal)
        editText=findViewById(R.id.username)
        continieButton=findViewById(R.id.buttonContinue)
        continieButton!!.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                var text=editText?.text.toString()
                if(text==null||text.isBlank()||text.isEmpty())
                {
                    Toast.makeText(applicationContext,"Please enter a valid user name",Toast.LENGTH_LONG)
                }
                else
                {
                    var user=makeUserDto(text)
                    addNewUserData(user)
                }

            }

        })

    }

    fun makeUserDto(name:String):UserDto
    {   var user:UserDto?=null
        if(name==null||name.isBlank()||name.isEmpty())
        {
            Toast.makeText(this,"Please enter a valid user name",Toast.LENGTH_LONG)
            return null!!
        }
        else
        {
            user= UserDto(name.toLowerCase(),"","")
            return user
        }


    }

    fun addNewUserData(user:UserDto)
    {
        progressBar?.visibility= View.VISIBLE
        if(user!=null)
        {
            fireBaseDatabaseRef?.child("Users")?.child(currentUser?.uid!!)?.setValue(user)?.addOnCompleteListener({
                if(it.isSuccessful)
                {
                    progressBar?.visibility= View.GONE
                    sendToUserProfilePictureSeletActivity()
                }
                else
                {
                    progressBar?.visibility= View.GONE
                    Toast.makeText(this,"Something Went Wrong  ", Toast.LENGTH_LONG).show()
                }
            })
        }
        else
        {
            Toast.makeText(this,"Something Went Wrong  ", Toast.LENGTH_LONG).show()

        }

    }

    fun sendToUserProfilePictureSeletActivity()
    {
        var i:Intent=Intent(this,NewUserGetProfilePictureActivity::class.java)
        startActivity(i)
    }
}