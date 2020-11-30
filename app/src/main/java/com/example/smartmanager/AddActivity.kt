package com.example.smartmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.example.smartmanager.model.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddActivity : AppCompatActivity() {

    val firebaseUser = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val userUID = FirebaseAuth.getInstance().currentUser?.uid;
        Toast.makeText(this, userUID.toString(), Toast.LENGTH_LONG).show()

        val uploadBtn = findViewById<Button>(R.id.buttonUploadActivity) as Button
        uploadBtn.setOnClickListener{
            val activityName : EditText = findViewById(R.id.editTextActivityName)
            val activityDescription : EditText = findViewById(R.id.editTextActivityDescription)
            val activityStartTime : EditText = findViewById(R.id.editTextTime)
            val activityDate : EditText = findViewById(R.id.editTextDate)
            val activityColor : EditText = findViewById(R.id.editTextColor)
            val reminder : CheckBox = findViewById(R.id.checkBoxReminder)

            val mName = activityName.text.toString()
            val mDescription = activityDescription.text.toString()
            val mTime = activityStartTime.text.toString()
            val mDate = activityDate.text.toString()
            val mColor = activityColor.text.toString()
            val mReminder = if(reminder.isChecked) 1 else 0
            val mCompleted = 0

            if(mName.isEmpty()){
                Toast.makeText(this, "Name is empty", Toast.LENGTH_SHORT).show()
            }else if(mDescription.isEmpty()){
                Toast.makeText(this, "Description is empty", Toast.LENGTH_SHORT).show()
            }else if(mTime.isEmpty()){
                Toast.makeText(this, "Time is empty", Toast.LENGTH_SHORT).show()
            }else if(mDate.isEmpty()) {
                Toast.makeText(this, "Date is empty", Toast.LENGTH_SHORT).show()
            }else if(mColor.isEmpty()) {
                Toast.makeText(this, "Color is empty", Toast.LENGTH_SHORT).show()
            }

            val userUID = firebaseUser.currentUser!!.uid
            //create activity obj

            //val ref = FirebaseDatabase.getInstance().getReference(userUID).child("Activities")
            val ref = FirebaseDatabase.getInstance().getReference("User").child(userUID).child("Activities")
            val activityId = ref.push().key.toString()
            val activity = Activity(activityId,mName, mDescription, mTime, mDate,mColor, mReminder, mCompleted)

            //send obj to firebase
            ref.child(activityId).setValue(activity)
            Toast.makeText(this,"activity added",Toast.LENGTH_SHORT).show()
            finish()
        }
    }

}