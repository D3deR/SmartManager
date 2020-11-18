package com.example.smartmanager.model

import java.sql.Date
import java.sql.Time

data class Activity(val id:String, val activityName:String,val description:String, val startTime:Time, val date:Date, val color:String, val reminder:Int, val completed:Int){
}