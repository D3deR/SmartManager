package com.example.smartmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class EmailLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_login)

        val email = findViewById<EditText>(R.id.login_email_id)
        val password  = findViewById<EditText>(R.id.login_password_id)
        val loginButton = findViewById<Button>(R.id.login_sign_in_btn)
        val goToRegister = findViewById<TextView>(R.id.RegisterButton)
        val auth = FirebaseAuth.getInstance()

        goToRegister.setOnClickListener{
            val login = Intent(this , RegisterScreen::class.java);
            startActivity(login)
            finish()
        }

        loginButton.setOnClickListener {
            val sEmail = email.text.toString().trim()
            val sPassword = password.text.toString().trim()
            auth.signInWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(this, OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
                    }
                })
        }

    }
}