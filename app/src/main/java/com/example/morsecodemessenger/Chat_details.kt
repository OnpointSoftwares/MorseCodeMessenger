package com.example.morsecodemessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.edugram.adapters.ChatsAdapter
import com.example.edugram.adapters.individual_chats_adapter
import com.example.morsecodemessenger.models.Chat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

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
        val dot:Button=findViewById(R.id.dot)
        val underscore:Button=findViewById(R.id.underscore)
        val space:Button=findViewById(R.id.space)
        val content:TextView=findViewById(R.id.content)
        val clear:Button=findViewById(R.id.clear)
        clear.setOnClickListener {
            content.text=""
        }
        dot.setOnClickListener {
            content.append(".")
        }
        space.setOnClickListener {
            content.append("/")
        }
        underscore.setOnClickListener {
            content.append("_")
        }
        content.setOnClickListener {
            morseToText(content.text.toString())
            content.text=""
        }
        sendBtn.setOnClickListener {
            val ref=FirebaseDatabase.getInstance().reference
            ref.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val PhoneNumberCurrent=snapshot.child("phone").value.toString()
                    val key=ref.child("Chats").push().key.toString()
                    if(PhoneNumberCurrent!="null") {
                        ref.child("Chats").child(key).child(phone)
                            .setValue(Chat(PhoneNumberCurrent, phone, textToSend.text.toString(),FirebaseAuth.getInstance().currentUser!!.uid))
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    ref.child("Chats").child(key).child(PhoneNumberCurrent)
                                        .setValue(
                                            Chat(
                                                PhoneNumberCurrent,
                                                phone,
                                                textToSend.text.toString()
                                            ,FirebaseAuth.getInstance().currentUser!!.uid
                                            )
                                        ).addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            Toast.makeText(
                                                this@Chat_details,
                                                "Message send successfully",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                }
                            }
                    }
                    else{
                        Toast.makeText(this@Chat_details, "Empty number ${FirebaseAuth.getInstance().currentUser!!.uid}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
        val user= FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance().reference.child("Users").child(user).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                findViewById<CircleImageView>(R.id.profilephoto).load(snapshot.child("profilePictureUrl").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        val chats=ArrayList<Chat>()
        ref.child("Chats").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chats.clear()
                for(snap in snapshot.children) {
                    if(snap.hasChild(phone)) {
                        val text = snap.child(phone).child("text").value.toString()
                        val sender = snap.child(phone).child("sender").value.toString()
                        val receiver = snap.child(phone).child("receiver").value.toString()
                        val uid=snap.child(phone).child("uid").value.toString()
                        chats.add(Chat(sender, receiver, text,uid))
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
    private fun morseToText(morse:String)
    {
        if(morse=="._")
        {
            findViewById<EditText>(R.id.text_to_send).append("a")
        }
        else if(morse=="_...")
        {
            findViewById<EditText>(R.id.text_to_send).append("b")
        }
        else if(morse=="_._.")
        {
            findViewById<EditText>(R.id.text_to_send).append("c")
        }
        else if(morse=="_..")
        {
            findViewById<EditText>(R.id.text_to_send).append("d")
        }
        else if(morse==".")
        {
            findViewById<EditText>(R.id.text_to_send).append("e")
        }
        else if(morse==".._.")
        {
            findViewById<EditText>(R.id.text_to_send).append("f")
        }
        else if(morse=="__.")
        {
            findViewById<EditText>(R.id.text_to_send).append("g")
        }
        else if(morse=="....")
        {
            findViewById<EditText>(R.id.text_to_send).append("h")
        }
        else if(morse=="..")
        {
            findViewById<EditText>(R.id.text_to_send).append("i")
        }
        else if(morse==".___")
        {
            findViewById<EditText>(R.id.text_to_send).append("j")
        }
        else if(morse=="_._")
        {
            findViewById<EditText>(R.id.text_to_send).append("k")
        }
        else if(morse=="._..")
        {
            findViewById<EditText>(R.id.text_to_send).append("l")
        }
        else if(morse=="__")
        {
            findViewById<EditText>(R.id.text_to_send).append("m")
        }
        else if(morse=="_.")
        {
            findViewById<EditText>(R.id.text_to_send).append("n")
        }
        else if(morse=="___")
        {
            findViewById<EditText>(R.id.text_to_send).append("o")
        }
        else if(morse==".__.")
        {
            findViewById<EditText>(R.id.text_to_send).append("p")
        }
        else if(morse=="__._")
        {
            findViewById<EditText>(R.id.text_to_send).append("q")
        }
        else if(morse=="._.")
        {
            findViewById<EditText>(R.id.text_to_send).append("r")
        }
        else if(morse=="...")
        {
            findViewById<EditText>(R.id.text_to_send).append("s")
        }
        else if(morse=="_")
        {
            findViewById<EditText>(R.id.text_to_send).append("t")
        }
        else if(morse==".._")
        {
            findViewById<EditText>(R.id.text_to_send).append("u")
        }
        else if(morse=="..._")
        {
            findViewById<EditText>(R.id.text_to_send).append("v")
        }
        else if(morse==".__")
        {
            findViewById<EditText>(R.id.text_to_send).append("w")
        }
        else if(morse=="_.._")
        {
            findViewById<EditText>(R.id.text_to_send).append("x")
        }
        else if(morse=="_.__")
        {
            findViewById<EditText>(R.id.text_to_send).append("y")
        }
        else if(morse=="__..")
        {
            findViewById<EditText>(R.id.text_to_send).append("z")
        }
        else if(morse==".____")
        {
            findViewById<EditText>(R.id.text_to_send).append("1")
        }
        else if(morse=="..___")
        {
            findViewById<EditText>(R.id.text_to_send).append("2")
        }
        else if(morse=="...__")
        {
            findViewById<EditText>(R.id.text_to_send).append("3")
        }
        else if(morse=="...._")
        {
            findViewById<EditText>(R.id.text_to_send).append("4")
        }
        else if(morse==".....")
        {
            findViewById<EditText>(R.id.text_to_send).append("5")
        }
        else if(morse=="_....")
        {
            findViewById<EditText>(R.id.text_to_send).append("6")
        }
        else if(morse=="__...")
        {
            findViewById<EditText>(R.id.text_to_send).append("7")
        }
        else if(morse=="___..")
        {
            findViewById<EditText>(R.id.text_to_send).append("8")
        }
        else if(morse=="____.")
        {
            findViewById<EditText>(R.id.text_to_send).append("9")
        }
        else if(morse=="_____")
        {
            findViewById<EditText>(R.id.text_to_send).append("0")
        }
        else if(morse=="/")
        {
            findViewById<EditText>(R.id.text_to_send).append(" ")
        }
    }
}