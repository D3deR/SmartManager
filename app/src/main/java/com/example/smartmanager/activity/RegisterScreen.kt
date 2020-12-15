package com.example.smartmanager.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.smartmanager.R
import com.example.smartmanager.model.Activity
import com.example.smartmanager.model.User

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_screen)
        val username = findViewById<EditText>(R.id.register_username_txt)
        val email = findViewById<EditText>(R.id.register_email_id)
        val password1  = findViewById<EditText>(R.id.register_password1_id)
        val password2 = findViewById<EditText>(R.id.register_password2_id)
        val registerButton = findViewById<Button>(R.id.register_btn)
        val goToLoginButton = findViewById<TextView>(R.id.LoginButton)
        val firebase = FirebaseAuth.getInstance()
        val ref = FirebaseDatabase.getInstance().getReference("User")

        goToLoginButton.setOnClickListener{
            val login = Intent(this , EmailLogin::class.java)
            startActivity(login)
            finish()
        }

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

                        //create user object
                        val userID = firebase.currentUser?.uid
                        val activityList:List<Activity>? = emptyList()
                        //val activityList:List<Activity>? = listOf<Activity>(Activity("123","mName", "mDescription", "mTime", "mDate","mColor", 0, 0),Activity("1234","mName", "mDescription", "mTime", "mDate","mColor", 0, 0))
                        val user = userID?.let { it1 -> User(it1,username.text.toString(),activityList ) }
                        //save object to firebase
                        if (userID != null) {
                            ref.child(userID).setValue(user)
                        }

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