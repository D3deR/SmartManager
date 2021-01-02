package com.example.smartmanager.model

data class Activity(val id:String?="", val activityName:String?="", val description:String?="", val startTime:String?="", val date:String?="", val color:String?="", val reminder:Int=0, var completed:Int=0, var priority:Int = -1){
}