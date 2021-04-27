package com.example.mochat.Activities

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.mochat.R
import com.example.mochat.utilitiies.CountryData
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    private var spinner: Spinner? = null
    private var editText: EditText? = null
    private var button:Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        spinner = findViewById(R.id.spinnerCountries)
        spinner?.setAdapter(
            ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                CountryData.countryNames
            )
        )
        editText = findViewById(R.id.editTextPhone) as EditText
        button=findViewById(R.id.buttonContinue) as Button
        button?.setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(v: View?) {
                val code: String =
                    CountryData.countryAreaCodes.get(spinner!!.getSelectedItemPosition())
                val number = editText?.getText().toString().trim { it <= ' ' }
                if (number.isEmpty() || number.length < 10) {
                    editText?.setError("Valid number is required")
                    editText?.requestFocus()
                    return
                }
                val phoneNumber = "+$code$number"

                val intent = Intent(this@LoginActivity, VerifyPhoneNumberActivity::class.java)
                intent.putExtra("phonenumber", phoneNumber)
                startActivity(intent)
            }
        })

        getPermissions()
    }

    private fun getPermissions() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            requestPermissions(arrayOf<String>(Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA),1)
        }
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
    }
