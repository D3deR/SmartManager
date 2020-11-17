package com.example.smartmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.smartmanager.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class RegisterScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_screen)
        val username = findViewById<EditText>(R.id.register_username_txt)
        val email = findViewById<EditText>(R.id.register_email_id)
        val password1  = findViewById<EditText>(R.id.register_password1_id)
        val password2 = findViewById<EditText>(R.id.register_password2_id)
        val registerButton = findViewById<Button>(R.id.register_btn)
        val firebase = FirebaseAuth.getInstance()

        registerButton.setOnClickListener{
           val sEmail = email.text.toString().trim()
            val sPassword = password1.text.toString().trim()
            var emptyEmail = false;
            var emptyPass = false;
            if(TextUtils.isEmpty(sEmail))
            {
                Toast.makeText(this@RegisterScreen , "Email required!" , Toast.LENGTH_SHORT).show()
                emptyEmail = true;
            }
            if(TextUtils.isEmpty(sPassword))
            {
                Toast.makeText(this@RegisterScreen , "Password required!" , Toast.LENGTH_SHORT).show()
                emptyPass = true;
            }
            if(!emptyEmail && !emptyPass)
            {
                firebase.createUserWithEmailAndPassword(sEmail , sPassword).addOnCompleteListener{task ->
                    if (task.isSuccessful)
                    {
                        Toast.makeText(this@RegisterScreen , "Auth successfull" , Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        Toast.makeText(this@RegisterScreen , "Auth failed" , Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}