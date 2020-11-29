package com.example.smartmanager.db_operations
import com.example.smartmanager.model.Activity
import com.google.firebase.database.FirebaseDatabase
import java.sql.Date
import java.sql.Time

class AddActivityToDB {
    fun saveActivity(activityName:String, description:String, startTime: Time, date: Date, color:String, reminder:Int, completed:Int){
        val ref = FirebaseDatabase.getInstance().getReference("Activity")
        val activityId = ref.push().key.toString()
        val activity = Activity(activityId,activityName, description, startTime, date,color, reminder, completed)
        ref.child(activityId).setValue(activity)
    }
}