package com.example.smartmanager.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.smartmanager.R
import com.example.smartmanager.model.Activity

class DetailActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        //toolbar back arrow
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        //get values from intent and create new Activity object
        val mId: String? = intent.getStringExtra("aId")
        val mActivityName: String? = intent.getStringExtra("aName")
        val mDescription: String? = intent.getStringExtra("aDescription")
        val mStartTime: String? = intent.getStringExtra("aStartTime")
        val mDate: String? = intent.getStringExtra("aDate")
        val mColor: String? = intent.getStringExtra("aColor")
        val mReminder: Int = intent.getIntExtra("aReminder", -1)
        val mCompleted: Int = intent.getIntExtra("aCompleted", -1)
        val mActivity = Activity(
            mId,
            mActivityName,
            mDescription,
            mStartTime,
            mDate,
            mColor,
            mReminder,
            mCompleted
        )


        //
        val colorBackgrLayout = findViewById<LinearLayout>(R.id.LinearLayout_activity_title)
        val actName = findViewById<TextView>(R.id.textView_activity_name)
        val actTime = findViewById<TextView>(R.id.textView_activity_time)
        val actDate = findViewById<TextView>(R.id.textView_activity_date)
        val actDescription = findViewById<TextView>(R.id.textView_activity_description)
        val actReminderSwitch = findViewById<Switch>(R.id.Switch_completed_detail)
        val actCompletedCheck = findViewById<CheckBox>(R.id.CheckBox_completed_detail)

        //set values
        //trebuie sa ii dau context ca sa nu mai fie depricated
        //( https://stackoverflow.com/questions/31590714/getcolorint-id-deprecated-on-android-6-0-marshmallow-api-23 )

        if (mActivity.color == "Blue") {
            colorBackgrLayout.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        } else if (mActivity.color == "Red") {
            colorBackgrLayout.setBackgroundColor(resources.getColor(R.color.redText))
        } else if (mActivity.color == "Yellow") {
            colorBackgrLayout.setBackgroundColor(resources.getColor(R.color.yellowText))
        }
        actName.text = mActivity.activityName
        actTime.text = mActivity.startTime
        actDate.text = mActivity.date
        actDescription.text = mActivity.description

        //make switch not clickable & set switch value
        actReminderSwitch.isClickable = false
        actReminderSwitch.isChecked = mActivity.reminder == 1

        //make checkbox not clickable & set checkbox value
        actCompletedCheck.isClickable = false
        actCompletedCheck.isChecked = mActivity.completed == 1

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            finish() // close this activity and return to previous activity (if there is any)
        }
        return super.onOptionsItemSelected(item)
    }

}