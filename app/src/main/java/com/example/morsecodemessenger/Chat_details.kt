package com.example.morsecodemessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edugram.adapters.ChatsAdapter
import com.example.edugram.adapters.individual_chats_adapter
import com.example.morsecodemessenger.models.Chat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Chat_details : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_details)
        val phone=intent.extras!!.getString("phone").toString()
        val rv=findViewById<RecyclerView>(R.id.chats_rv)
        val ref= FirebaseDatabase.getInstance().reference
        val textToSend:EditText=findViewById(R.id.text_to_send)
        val sendBtn:FloatingActionButton=findViewById(R.id.sendBtn)
        findViewById<TextView>(R.id.phone).text=phone
        sendBtn.setOnClickListener {
            val ref=FirebaseDatabase.getInstance().reference
           val key=ref.child("Chats").push().key.toString()
            ref.child("Chats").child(key).child(phone).setValue(Chat(FirebaseAuth.getInstance().currentUser!!.uid.toString(),phone,textToSend.text.toString())).addOnCompleteListener {
                if(it.isSuccessful)
                {
                    Toast.makeText(this,"Message send successfully",Toast.LENGTH_LONG).show()
                }
            }
        }
        val user= FirebaseAuth.getInstance().currentUser!!.uid
        val chats=ArrayList<Chat>()
        ref.child("Chats").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chats.clear()
                for(snap in snapshot.children) {
                    if(snap.hasChild(phone)) {
                        val text = snap.child(phone).child("text").value.toString()
                        val sender = snap.child(phone).child("sender").value.toString()
                        val receiver = snap.child(phone).child("receiver").value.toString()
                        chats.add(Chat(sender, receiver, text))
                        val chatsAdapter = individual_chats_adapter(chats)
                        rv.layoutManager = LinearLayoutManager(this@Chat_details)
                        val itemAnimator = DefaultItemAnimator()
                        rv.itemAnimator = itemAnimator
                        rv.setHasFixedSize(true)
                        rv.adapter = chatsAdapter
                        rv.adapter!!.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }
}