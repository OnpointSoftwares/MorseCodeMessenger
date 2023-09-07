package com.example.morsecodemessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnLogin:MaterialButton=findViewById(R.id.btn_login)
        val btnSignup:MaterialButton=findViewById(R.id.btn_signup)
        val email:EditText=findViewById(R.id.etEmail)
        val password:EditText=findViewById(R.id.edt_password)
        btnLogin.setOnClickListener {
            handleLogin(email.text.toString(),password.text.toString())
        }
        btnSignup.setOnClickListener {
            startActivity(Intent(this,Registration::class.java))
        }
    }

    private fun handleLogin(email: String, password: String) {
        val auth=FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful)
            {
                Toast.makeText(this,"Login Successful",Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this,"Error logging in",Toast.LENGTH_LONG).show()
            }
        }
    }
}