package com.example.smartmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

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
            val sPassword2 = password2.text.toString().trim()
            val sUsername = username.text.toString().trim()
            var emptyEmail = false
            var emptyPass = false
            var matchPass = true
            var emptyUser = false
            var validEmail = false
            var longPass =true

            if(TextUtils.isEmpty(sEmail))
            {
                Toast.makeText(this@RegisterScreen , "Email required!" , Toast.LENGTH_SHORT).show()
                emptyEmail = true
            }
            if(TextUtils.isEmpty(sPassword))
            {
                Toast.makeText(this@RegisterScreen , "Password required!" , Toast.LENGTH_SHORT).show()
                emptyPass = true
            }
            if(sPassword != sPassword2)
            {
                Toast.makeText(this@RegisterScreen , "Password do not match!" , Toast.LENGTH_SHORT).show()
                matchPass = false
            }
            if(TextUtils.isEmpty(sUsername))
            {
                Toast.makeText(this@RegisterScreen , "Username required!" , Toast.LENGTH_SHORT).show()
                emptyUser = true
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(sEmail).matches())
            {
                Toast.makeText(this@RegisterScreen , "E-mail not recognized!" , Toast.LENGTH_SHORT).show()
                validEmail = false
            }
            if(sPassword.length < 6)
            {
                Toast.makeText(this@RegisterScreen , "Password is too short!" , Toast.LENGTH_SHORT).show()
                longPass = false
            }
            if(!emptyEmail && !emptyPass && !emptyUser && matchPass && !validEmail && longPass)
            {
                firebase.createUserWithEmailAndPassword(sEmail , sPassword).addOnCompleteListener{task ->
                    if (task.isSuccessful)
                    {
                        firebase.currentUser?.sendEmailVerification()?.addOnCompleteListener{ task->}
                        if(task.isSuccessful) {
                            Toast.makeText(
                                this@RegisterScreen,
                                "Auth successfull,check email",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
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