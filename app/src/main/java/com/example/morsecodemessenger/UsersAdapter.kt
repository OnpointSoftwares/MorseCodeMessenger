package com.example.edugram.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.morsecodemessenger.Chat_details
import com.example.morsecodemessenger.R
import com.example.morsecodemessenger.models.Chat
import com.example.morsecodemessenger.models.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class UsersAdapter(private val users: List<user>) :
    RecyclerView.Adapter<UsersAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user1: user) {
            itemView.findViewById<TextView>(R.id.phone).text= user1.phone
            itemView.setOnClickListener {
                val intent=Intent(itemView.context,Chat_details::class.java)
                intent.putExtra("phone",user1.phone)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)
        return NotificationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val user=users[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return users.size
    }
}
