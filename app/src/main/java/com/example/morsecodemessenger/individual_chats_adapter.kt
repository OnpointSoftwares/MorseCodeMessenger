package com.example.edugram.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.morsecodemessenger.R
import com.example.morsecodemessenger.models.Chat
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
            itemView.findViewById<TextView>(R.id.username).text = chats.sender
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
