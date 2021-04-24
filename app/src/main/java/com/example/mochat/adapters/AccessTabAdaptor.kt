package com.example.mochat.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mochat.fragments.ChatsFragment
import com.example.mochat.fragments.ContactsFragment
import com.example.mochat.fragments.GroupChatFragment

class AccessTabAdaptor:FragmentStateAdapter
{
    constructor(fa:FragmentActivity): super(fa)


    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when(position)
        {
            0-> return ChatsFragment()
            1-> return GroupChatFragment()
            2->return ContactsFragment()
            else-> return null!!
        }
    }


}