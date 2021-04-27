package com.example.mochat.adapters

import android.R.attr.src
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mochat.R
import com.example.mochat.domain.UserDto
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class RecycleViewAdapter(private val dataSet: ArrayList<UserDto>) :
    RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contactsName: TextView
        val contactsStatus:TextView
        val contactImage:CircleImageView
        init {
            // Define click listener for the ViewHolder's View.
            contactsName = view.findViewById(R.id.contacts_name)
            contactsStatus=view.findViewById(R.id.contact_status)
            contactImage=view.findViewById(R.id.contact_image)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.contacts_adapter_view, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.contactsName.text = dataSet.get(position).name
        viewHolder.contactsStatus.text=dataSet.get(position).userStatus
        if(dataSet.get(position).profilePicUrl!="")
        {   var url=dataSet.get(position).profilePicUrl
             try {
                val url = URL(url)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.setDoInput(true)
                connection.connect()
                val input: InputStream = connection.getInputStream()
                val myBitmap = BitmapFactory.decodeStream(input)
                 viewHolder.contactImage.setImageBitmap(myBitmap)
            } catch (e: IOException) {
                e.printStackTrace()


            }


        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}