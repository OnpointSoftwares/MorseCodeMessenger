package com.example.morsecodemessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edugram.adapters.ChatsAdapter
import com.example.morsecodemessenger.models.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Chats : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)
        val rv=findViewById<RecyclerView>(R.id.chats)
        val ref=FirebaseDatabase.getInstance().reference
        val user="FirebaseAuth.getInstance().currentUser!!.uid"
        val chats=ArrayList<Chat>()
        /*ref.child("Chats").child(user).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val text=snapshot.child("text").value.toString()
                val sender=snapshot.child("sender").value.toString()
                val receiver=snapshot.child("receiver").value.toString()
               chats.add(Chat(sender,receiver,text))
                rv.adapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })*/
        chats.add(Chat("Vincent","abcsbsj","Goodmorning"))
        chats.add(Chat("Victor","abcsbsj","Goodmorning"))
        val chatsAdapter = ChatsAdapter(chats)
        rv.layoutManager = LinearLayoutManager(this)
        val itemAnimator = DefaultItemAnimator()
        rv.itemAnimator = itemAnimator
        rv.setHasFixedSize(true)
        rv.adapter=chatsAdapter
        val adapter=ChatsAdapter(chats)
        rv.adapter=adapter

    }
}