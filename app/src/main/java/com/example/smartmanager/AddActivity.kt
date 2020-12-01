package com.example.smartmanager

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.smartmanager.model.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

class AddActivity : AppCompatActivity() {

    val firebaseUser = FirebaseAuth.getInstance()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val spinner:Spinner = findViewById(R.id.editTextColor)
        ArrayAdapter.createFromResource(this , R.array.color_array , android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter }

        val timeBtn  = findViewById<Button>(R.id.timeBtn)
        val dateBtn = findViewById<Button>(R.id.dateBtn)
        val timeView = findViewById<TextView>(R.id.editTextTime)
        val dateView = findViewById<TextView>(R.id.editTextDate)
        timeBtn.setOnClickListener{
                val cal = Calendar.getInstance()
                val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    timeView.text = SimpleDateFormat("HH:mm").format(cal.time)
                }
                TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            }

        dateBtn.setOnClickListener{
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
            val activityName : EditText = findViewById(R.id.editTextActivityName)
            val activityDescription : EditText = findViewById(R.id.editTextActivityDescription)
            val activityStartTime : TextView = findViewById(R.id.editTextTime)
            val activityDate : TextView = findViewById(R.id.editTextDate)
            //val activityColor : EditText = findViewById(R.id.editTextColor)
            val reminder : CheckBox = findViewById(R.id.checkBoxReminder)

            val mName = activityName.text.toString()
            val mDescription = activityDescription.text.toString()
            val mTime = activityStartTime.text.toString()
            val mDate = activityDate.text.toString()
            val mColor = spinner.selectedItem.toString();
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

            //val ref = FirebaseDatabase.getInstance().getReference(userUID).child("Activities")
            val ref = FirebaseDatabase.getInstance().getReference("User").child(userUID).child("Activities")
            val activityId = ref.push().key.toString()

            //create activity obj
            val activity = Activity(activityId,mName, mDescription, mTime, mDate,mColor, mReminder, mCompleted)

            //send obj to firebase
            ref.child(activityId).setValue(activity)
            Toast.makeText(this,"activity added",Toast.LENGTH_SHORT).show()
            finish()
        }
    }

}