package com.example.morsecodemessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edugram.adapters.ChatsAdapter
import com.example.edugram.adapters.UsersAdapter
import com.example.morsecodemessenger.models.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.ArrayList

class SelectUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_user)
        val rv1:RecyclerView=findViewById(R.id.rv)
        val ref= FirebaseDatabase.getInstance().reference
        val userlist:ArrayList<user>
        userlist= ArrayList()
        ref.child("Users").addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children)
                {
                    val phone=snap.child("phone").value.toString()
                    val username=snap.child("username").value.toString()
                    val email=snap.child("email").value.toString()
                    val password=snap.child("password").value.toString()
                    val profilepic=snap.child("profilePictureUrl").value.toString()
                    val user1=user(phone,username,email,password,profilepic)
                    userlist.add(user1)
                    val usersAdapter = UsersAdapter(userlist)
                    rv1.layoutManager = LinearLayoutManager(this@SelectUser)
                    val itemAnimator = DefaultItemAnimator()
                    rv1.itemAnimator = itemAnimator
                    rv1.setHasFixedSize(true)
                    rv1.adapter=usersAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}