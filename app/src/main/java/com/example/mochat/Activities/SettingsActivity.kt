package com.example.mochat.Activities

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mochat.R
import com.example.mochat.domain.UserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import de.hdodenhof.circleimageview.CircleImageView


class SettingsActivity:AppCompatActivity() {
    var button:Button?=null
    var circleImageView:CircleImageView?=null
    var userNameEdittext:EditText?=null
    var statusEdittext:EditText?=null
    var progressBar:ProgressBar?=null
    var userNameFromDb:String?= null
    var userStatusFromDb:String?=null
    var currentUser:FirebaseUser?=null
    var databaseRef:DatabaseReference?=null
    var userDto:UserDto?=null
    var calledAfterUpdate:Boolean?=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        userNameEdittext = findViewById(R.id.username_edit_text)
        statusEdittext = findViewById(R.id.status_edit_text)
        progressBar = findViewById(R.id.progressBar)
        currentUser = FirebaseAuth.getInstance().currentUser
        databaseRef = FirebaseDatabase.getInstance().reference
        button=findViewById(R.id.update_button)
        button?.setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                update()
            }
        })
        getDataFromDatabase()
    }

    fun getDataFromDatabase()
    {
        progressBar?.visibility = View.VISIBLE
        databaseRef?.child(getString(R.string.basePathForUser))?.child(currentUser?.uid!!)?.get()?.addOnSuccessListener ({
            progressBar?.visibility= View.INVISIBLE
            val userInfo=it.value as HashMap<String,String>
            userDto=UserDto(userInfo.get("name")!!,"",userInfo.get("userStatus")!!)
            Log.d("userInfo",userDto.toString())
            userNameFromDb=userDto?.name
            userStatusFromDb=userDto?.userStatus
            userNameEdittext?.setText(userNameFromDb)
            statusEdittext?.setText(userStatusFromDb)
            userNameEdittext?.hint=userNameFromDb
            if(userStatusFromDb==null||userStatusFromDb=="")
            {
                statusEdittext?.hint="status"
            }
            else
            {
                statusEdittext?.hint=userStatusFromDb
            }
            statusEdittext?.hint=userStatusFromDb
            if(calledAfterUpdate!!)
            {
                Toast.makeText(this,"Data updated successfully",Toast.LENGTH_LONG).show()
                calledAfterUpdate=false

            }
        })?.addOnFailureListener({
            progressBar?.visibility= View.INVISIBLE
            if(calledAfterUpdate!!)
            {
                calledAfterUpdate=false
                Toast.makeText(this,"Error Updating data Check your Internet Connection", Toast.LENGTH_LONG)
            }
            else
            {
                Toast.makeText(this,"Error getting data", Toast.LENGTH_LONG)
            }

        })

    }
    fun update()
    {
        var userNameFromEditText=userNameEdittext?.text.toString()
        var statusFromEdittext=statusEdittext?.text.toString()
        if(userNameFromEditText==null||userNameFromEditText=="")
        {
            Toast.makeText(this,"Please give a valid userName",Toast.LENGTH_LONG).show()
        }
       else
        {
            if(userNameFromEditText!=userNameFromDb&&userStatusFromDb!=statusFromEdittext)
            {
                Toast.makeText(this,"Nothing to update",Toast.LENGTH_LONG).show()

            }
            else
            {
                userDto?.name=userNameFromEditText
                userDto?.userStatus=statusFromEdittext
                progressBar?.visibility= View.VISIBLE
                databaseRef?.child(getString(R.string.basePathForUser))?.child(currentUser?.uid!!)?.setValue(userDto)?.addOnCompleteListener({
                    if(it.isSuccessful)
                    {
                        progressBar?.visibility= View.INVISIBLE
                        calledAfterUpdate=true
                        getDataFromDatabase()

                    }
                    else
                    {
                        progressBar?.visibility= View.INVISIBLE
                        Toast.makeText(this,"Something Went Wrong  ", Toast.LENGTH_LONG).show()
                    }
                })


            }
        }
    }

    override fun onBackPressed() {

        if(userNameEdittext?.text.toString().trim()!=userNameFromDb||userStatusFromDb!=statusEdittext?.text.toString().trim())
        {
            var builder:AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage("There are some unsaved changes do you want to leave this page ?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, id ->
                        super.onBackPressed()
                    })
                .setNegativeButton("No",
                    DialogInterface.OnClickListener { dialog, id -> //  Action for 'NO' Button
                        dialog.cancel()
                    }).show()
        }
        else
        {
            super.onBackPressed()
        }
    }

    }
