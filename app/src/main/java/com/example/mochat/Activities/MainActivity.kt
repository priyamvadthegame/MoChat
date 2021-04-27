package com.example.mochat.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.mochat.R
import com.example.mochat.adapters.AccessTabAdaptor
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private var mToolbar:MaterialToolbar?=null
    private var mViewPager:ViewPager2?=null
    private var viewPagerAdapter:AccessTabAdaptor?=null
    private var mTabLayout:TabLayout?=null
    private var mAuth:FirebaseAuth?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mToolbar=findViewById(R.id.main_page_toolbar) as MaterialToolbar
        setSupportActionBar(mToolbar)
        supportActionBar?.setTitle(R.string.app_name)
        mViewPager=findViewById(R.id.main_page_viewpager) as ViewPager2
        viewPagerAdapter= AccessTabAdaptor(this)
        mViewPager?.adapter=viewPagerAdapter
        mAuth= FirebaseAuth.getInstance()
        mTabLayout=findViewById(R.id.main_page_tablayout) as TabLayout
        TabLayoutMediator(mTabLayout!!, mViewPager!!) { tab, position ->
            when(position) {
                0 -> tab.text = "Chats"
                1 -> tab.text = "Group Chats"
                2 -> tab.text = "Contacts"
                else -> tab.text = ""
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         super.onCreateOptionsMenu(menu)
            menuInflater.inflate(R.menu.options_menu,menu)
            return true
    }

    fun redirectToLoginActivity()
    {
        var i:Intent=Intent(this,LoginActivity::class.java)
        startActivity(i)
    }
    fun redirectToSettingsActivity()
    {
        var i:Intent=Intent(this,SettingsActivity::class.java)
        startActivity(i)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         super.onOptionsItemSelected(item)
        when(item.itemId)
        {
            R.id.findFriends->{

            }
                R.id.settings->{
                        redirectToSettingsActivity()
                }
            R.id.logout->{
                mAuth?.signOut()
                redirectToLoginActivity()
            }
            else->
            {
                Toast.makeText(this,"Select a proper option",Toast.LENGTH_LONG).show()
            }
        }
        return true
    }
}