package com.example.morsecodemessenger

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
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

class Chats : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)
        val rv = findViewById<RecyclerView>(R.id.chats)
        val logout:ImageView=findViewById(R.id.logout)
        logout.setOnClickListener {
            val builder=AlertDialog.Builder(this)
            builder.setMessage("Do you want to logout?")
            builder.setTitle("Logout")
            builder.setNegativeButton("Cancel"){dialog,_ ->
                dialog.dismiss()
            }
            builder.setPositiveButton("Yes"){dialog,_ ->
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this,MainActivity::class.java))
            }
            builder.create().show()
        }
        val ref = FirebaseDatabase.getInstance().reference
        val chats = ArrayList<Chat>()
        val newMessage: FloatingActionButton =
            findViewById(R.id.newmessagebtn)
        newMessage.setOnClickListener {
            startActivity(Intent(this@Chats, SelectUser::class.java))
        }
        val user= FirebaseAuth.getInstance().currentUser!!.uid
        ref.child("Users").child(user).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(datasnapshot: DataSnapshot) {
                val phone=datasnapshot.child("phone").value.toString()
                ref.child("Chats").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        chats.clear()
                        for(snap in snapshot.children) {
                            if(snap.child(phone).child("sender").value.toString()==phone||snap.child(phone).child("receiver").value.toString()==phone) {
                                val text = snap.child(phone).child("text").value.toString()
                                val sender = snap.child(phone).child("sender").value.toString()
                                val receiver = snap.child(phone).child("receiver").value.toString()
                                chats.add(Chat(sender, receiver, text,snap.child(phone).child("uid").value.toString()))
                                val chatsAdapter = ChatsAdapter(chats)
                                rv.layoutManager = LinearLayoutManager(this@Chats)
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

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }
}

