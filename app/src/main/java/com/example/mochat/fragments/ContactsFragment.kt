package com.example.mochat.fragments

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mochat.R
import com.example.mochat.adapters.RecycleViewAdapter
import com.example.mochat.domain.UserDto

class ContactsFragment : Fragment() {

    var recyclerView:RecyclerView?=null
    var recyclerViewAdapter:RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>?=null
    var layoutMnager:RecyclerView.LayoutManager?=null
    var contactsList:ArrayList<UserDto>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_contacts, container, false)
        getContactLists(view)
        return view
    }

    private fun getContactLists(view:View) {
        var contacts:Cursor= context?.contentResolver?.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null)!!
        while (contacts.moveToNext())
        {
            var name:String=contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            var number:String=contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            var makeUserDto:UserDto= UserDto(name,number,"")
            contactsList?.add(makeUserDto)
        }
        initialiZeRecyclerView(view)
    }

    fun initialiZeRecyclerView(view:View)
    {
        recyclerView=view.findViewById(R.id.contacts_list1)
        recyclerView?.isNestedScrollingEnabled=false
        recyclerView?.setHasFixedSize(false)
        layoutMnager=LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        recyclerView?.layoutManager=layoutMnager
        recyclerViewAdapter=RecycleViewAdapter(contactsList!!)
        recyclerView?.adapter=recyclerViewAdapter
    }

    companion object {

        fun newInstance(): ContactsFragment {
            return ContactsFragment()
        }
    }

}