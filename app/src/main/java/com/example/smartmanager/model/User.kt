package com.example.smartmanager.model

data class User(val id:String, val userName:String, val activityList:List<Activity>? = emptyList()){
}