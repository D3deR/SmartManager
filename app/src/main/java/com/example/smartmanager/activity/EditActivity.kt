package com.example.smartmanager.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.smartmanager.R
import com.example.smartmanager.model.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*


class EditActivity : AppCompatActivity() {
    private val firebaseUser = FirebaseAuth.getInstance()
    private val userUID = firebaseUser.currentUser!!.uid

    @SuppressLint("UseSwitchCompatOrMaterialCode", "SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        //toolbar back arrow
        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        //create new Activity from intent
        val mId:String?= intent.getStringExtra("aId")
        val mActivityName:String?= intent.getStringExtra("aName")
        val mDescription:String?= intent.getStringExtra("aDescription")
        val mStartTime:String?=intent.getStringExtra("aStartTime")
        val mDate:String?= intent.getStringExtra("aDate")
        val mColor:String?=intent.getStringExtra("aColor")
        val mReminder:Int=intent.getIntExtra("aReminder", -1)
        val mCompleted:Int= intent.getIntExtra("aCompleted", -1)
        val mActivity = Activity(mId,mActivityName,mDescription,mStartTime,mDate, mColor, mReminder, mCompleted)

        //
        val nameEditTxt= findViewById<EditText>(R.id.editText_activity_name)
        val descriptionEditTxt = findViewById<EditText>(R.id.editText_description)
        val timeEditTxt = findViewById<EditText>(R.id.editText_time)
        val dateEditTxt = findViewById<EditText>(R.id.editText_date)
        val colorSpinner = findViewById<Spinner>(R.id.editText_color)
        val reminderSwitch = findViewById<Switch>(R.id.switch_reminder)
        val completedCheck = findViewById<CheckBox>(R.id.checkBox_completed)

        //set values
        nameEditTxt.setText(mActivity.activityName)
        descriptionEditTxt.setText(mActivity.description)
        timeEditTxt.setText(mActivity.startTime)
        dateEditTxt.setText(mActivity.date)
        //colorSpinner
        ArrayAdapter.createFromResource(this ,
            R.array.color_array, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            colorSpinner.adapter = adapter }
        if(mColor == "Red") {colorSpinner.setSelection(0)}
        else if(mColor == "Blue"){colorSpinner.setSelection(1)}
        else if(mColor == "Yellow"){colorSpinner.setSelection(2)}
        reminderSwitch.isChecked = mActivity.reminder == 1
        completedCheck.isChecked = (mActivity.completed == 1)

        //edit time and date
        val timeBtn = findViewById<Button>(R.id.btn_time)
        timeBtn.setOnClickListener{
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                timeEditTxt.setText(SimpleDateFormat("HH:mm").format(cal.time))
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
        val dateBtn = findViewById<Button>(R.id.btn_date)
        dateBtn.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                dateEditTxt.setText("$dayOfMonth/$monthOfYear/$year")
            }, year, month, day)
            dpd.show()
        }

        //Update
        val updateBtn = findViewById<Button>(R.id.btn_update)
        updateBtn.setOnClickListener {
            val editName = nameEditTxt.text.toString()
            val editDescription = descriptionEditTxt.text.toString()
            val editTime = timeEditTxt.text.toString()
            val editDate = dateEditTxt.text.toString()
            val editColor = colorSpinner.selectedItem.toString()
            var editReminder = 0
            if(reminderSwitch.isChecked){ editReminder = 1}
            var editCompleted = 0
            if(completedCheck.isChecked){editCompleted = 1}
            if(editName.isEmpty()){
                Toast.makeText(this, "Name is empty", Toast.LENGTH_SHORT).show()
            }else if(editDescription.isEmpty()){
                Toast.makeText(this, "Description is empty", Toast.LENGTH_SHORT).show()
            }else if(editTime.isEmpty()){
                Toast.makeText(this, "Time is empty", Toast.LENGTH_SHORT).show()
            }else if(editDate.isEmpty()) {
                Toast.makeText(this, "Date is empty", Toast.LENGTH_SHORT).show()
            }else if(editColor.isEmpty()) {
                Toast.makeText(this, "Color is empty", Toast.LENGTH_SHORT).show()
            }



            val ref = FirebaseDatabase.getInstance().getReference("User").child(userUID).child("Activities")
            val editedActivity = Activity(mId,editName,editDescription,editTime,editDate, editColor, editReminder, editCompleted)
            ref.child(mId!!).setValue(editedActivity)
            Toast.makeText(this,"activity edited",Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            finish() // close this activity and return to previous activity (if there is any)
        }
        return super.onOptionsItemSelected(item)
    }
}