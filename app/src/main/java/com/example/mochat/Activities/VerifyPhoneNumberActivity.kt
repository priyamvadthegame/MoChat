package com.example.mochat.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mochat.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.database.*
import java.util.concurrent.TimeUnit


class VerifyPhoneNumberActivity:AppCompatActivity(){
    private var verificationId: String? = null
    private var mAuth: FirebaseAuth? = null
    private var progressBar: ProgressBar? = null
    private var editText: EditText? = null
    private var textView: TextView?=null
    private var rootRef :DatabaseReference?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.verify_phone_activity)
        mAuth = FirebaseAuth.getInstance()
        progressBar = findViewById(R.id.progressbar)
        editText = findViewById(R.id.editTextCode)
        textView=findViewById(R.id.textView)
        rootRef=FirebaseDatabase.getInstance().reference
        val phonenumber = intent.getStringExtra("phonenumber")
        sendVerificationCode(phonenumber)
        findViewById<View>(R.id.buttonSignIn).setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(v: View?) {
                progressBar!!.visibility = View.VISIBLE
                val code = editText?.getText().toString().trim { it <= ' ' }
                if (code.isEmpty() || code.length < 6) {
                    editText?.setError("Enter code...")
                    editText?.requestFocus()
                    return
                }
                verifyCode(code)
            }
        })
    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result!!.user
                    rootRef?.child(getString(R.string.basePathForUser))?.child(user.uid)?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            progressBar!!.visibility = View.INVISIBLE
                            Toast.makeText(
                                    this@VerifyPhoneNumberActivity,
                                    error.message,
                                    Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            progressBar!!.visibility = View.INVISIBLE
                            Log.d("checkUser","inside it")
                            if (snapshot.value!=null) {
                                val intent =
                                        Intent(this@VerifyPhoneNumberActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }
                            else
                            {
                                var i:Intent= Intent(this@VerifyPhoneNumberActivity,NewUserGetDetailsActivity::class.java)
                                startActivity(i)
                            }
                        }
                    })
                } else {
                    progressBar!!.visibility = View.INVISIBLE
                    Toast.makeText(
                        this@VerifyPhoneNumberActivity,
                        task.exception!!.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun sendVerificationCode(number: String?) {
        progressBar!!.visibility = View.VISIBLE
        Log.d("phone number",number!!)
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(number)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallBack: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(
                s: String,
                forceResendingToken: ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)
                progressBar!!.visibility = View.INVISIBLE
                textView!!.visibility=View.VISIBLE
                verificationId = s
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                progressBar!!.visibility = View.INVISIBLE
                textView!!.visibility=View.VISIBLE
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    editText!!.setText(code)
                    verifyCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@VerifyPhoneNumberActivity, e.message, Toast.LENGTH_LONG).show()
                progressBar!!.visibility = View.INVISIBLE
            }
        }
}