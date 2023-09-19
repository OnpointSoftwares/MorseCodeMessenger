package com.example.edugram.adapters

import android.R.*
import android.R.color.*
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.morsecodemessenger.R
import com.example.morsecodemessenger.models.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class individual_chats_adapter(private val chat: List<Chat>) :
    RecyclerView.Adapter<individual_chats_adapter.chatViewHolder>() {

    class chatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(chats: Chat) {
            val ref=FirebaseDatabase.getInstance().reference
            itemView.findViewById<TextView>(R.id.text).text= chats.text
            ref.child("Users").child(chats.uid).addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(chats.uid==FirebaseAuth.getInstance().currentUser!!.uid.toString())
                    {
                        itemView.findViewById<TextView>(R.id.username).text = "Me"
                    }
                    else {
                        itemView.findViewById<TextView>(R.id.username).text =
                            snapshot.child("phone").value.toString()
                    }
                    itemView.findViewById<CircleImageView>(R.id.profile_pic).load(snapshot.child("profilePictureUrl").value.toString())
                    }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): chatViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_item, parent, false)
        return chatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: chatViewHolder, position: Int) {
        val chat = chat[position]
        holder.bind(chat)
    }

    override fun getItemCount(): Int {
        return chat.size
    }
}
