package com.example.smartmanager.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.smartmanager.R
import com.example.smartmanager.model.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.sql.Time
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {
//zzzzzzzzzz
    val firebaseUser = FirebaseAuth.getInstance()

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val spinner:Spinner = findViewById(R.id.editTextColor)
        ArrayAdapter.createFromResource(this ,
            R.array.color_array, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter }

        val prioritySpinner = findViewById<Spinner>(R.id.prioritySpinner)
        ArrayAdapter.createFromResource(this, R.array.priority_array ,android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            prioritySpinner.adapter = adapter
        }

        val timeView = findViewById<TextView>(R.id.editTextTime)
        val dateView = findViewById<TextView>(R.id.editTextDate)
        val sdf1 = SimpleDateFormat("HH:mm")
        val currentTime = sdf1.format(Date())
        val sdf2 = SimpleDateFormat("dd/MM/yyyy")
//        var datePlusOneMonth = Calendar.getInstance().run {
//            add(Calendar.MONTH, 1)
//            time
//        }
//        datePlusOneMonth = sdf2.parse(datePlusOneMonth.toString())!!
        val currentDate = sdf2.format(Date())

        timeView.text = currentTime
        timeView.setOnClickListener{
                val cal = Calendar.getInstance()
                val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    timeView.text = SimpleDateFormat("HH:mm").format(cal.time)
                }
                TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            }

        dateView.text = currentDate
        dateView.setOnClickListener{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                dateView.text="$dayOfMonth/$monthOfYear/$year"
            }, year, month, day)
            dpd.show()
        }

        val uploadBtn = findViewById<Button>(R.id.buttonUploadActivity) as Button
        uploadBtn.setOnClickListener{
            val activityName : EditText = findViewById(R.id.editTextName)
            val activityDescription : EditText = findViewById(R.id.editTextActivityDescription)
            val activityStartTime : TextView = findViewById(R.id.editTextTime)
            val activityDate : TextView = findViewById(R.id.editTextDate)
            //val activityColor : EditText = findViewById(R.id.editTextColor)
            val reminder : Switch = findViewById(R.id.reminderSwitch)

            val mName = activityName.text.toString()
            val mDescription = activityDescription.text.toString()
            val mTime = activityStartTime.text.toString()
            val mDate = activityDate.text.toString()
            val mColor = spinner.selectedItem.toString()
            val mReminder = if(reminder.isChecked) 1 else 0
            val mCompleted = 0
            var mPriority  = 0
            if(prioritySpinner.selectedItem.toString() == "Low"){
                mPriority = 1
            }else if(prioritySpinner.selectedItem.toString() == "Medium"){
                mPriority = 2
            }else if(prioritySpinner.selectedItem.toString() == "High"){
                mPriority = 3
            }

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
            }else if(mPriority == 0){
                Toast.makeText(this, "Priority is empty", Toast.LENGTH_SHORT).show()
            }

            val userUID = firebaseUser.currentUser!!.uid

            //val ref = FirebaseDatabase.getInstance().getReference(userUID).child("Activities")
            val ref = FirebaseDatabase.getInstance().getReference("User").child(userUID).child("Activities")
            val activityId = ref.push().key.toString()

            //create activity obj
            val activity = Activity(activityId,mName, mDescription, mTime, mDate,mColor, mReminder, mCompleted, mPriority)

            //send obj to firebase
            ref.child(activityId).setValue(activity)
            Toast.makeText(this,"activity added",Toast.LENGTH_SHORT).show()
            finish()
        }
    }

}