package com.example.mochat.Activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mochat.R
import org.w3c.dom.Text

class NewUserGetProfilePictureActivity:AppCompatActivity() {
    var skipText:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user_add_profile_picture)
        skipText=findViewById(R.id.skip_text)
        skipText?.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {

                var i:Intent= Intent(this@NewUserGetProfilePictureActivity,MainActivity::class.java)
                startActivity(i)
            }

        })

    }
}