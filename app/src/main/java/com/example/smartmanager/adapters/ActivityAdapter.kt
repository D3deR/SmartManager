package com.example.smartmanager.adapters

import android.app.PendingIntent.getActivity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmanager.R
import com.example.smartmanager.model.Activity
import com.example.smartmanager.ui.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_list_row.view.*

class ActivityAdapter(
    private val dataSet: MutableList<Activity>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<ActivityAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val activityRow = layoutInflater.inflate(R.layout.activity_list_row, parent, false)
        return ViewHolder(activityRow)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activityName = holder.itemView.textView_name
        activityName.text = dataSet[position].activityName

        val activityStartTime = holder.itemView.textView_time
        activityStartTime.text = dataSet[position].startTime

        val activityDescription = holder.itemView.textView_description
        activityDescription.text = dataSet[position].description

        val activityDate = holder.itemView.textView_date
        activityDate.text = dataSet[position].date

        val activityColor = holder.itemView.textView_color
        activityColor.text = dataSet[position].color

        val activityReminder = holder.itemView.reminder
        if (dataSet[position].reminder == 1) {
            activityReminder.text = R.string.reminder_active.toString()
        } else {
            activityReminder.text = R.string.reminder_inactive.toString()
        }

        val completedBox = holder.itemView.completed
        if (dataSet[position].completed == 1) {
            completedBox.isChecked = true
        }
        completedBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val userUID = FirebaseAuth.getInstance().currentUser?.uid
                val ref = userUID?.let {
                    FirebaseDatabase.getInstance().getReference("User").child(it).child("Activities")
                }
                val completed = dataSet[position]
                completed.completed = 1

                if (ref != null) {
                    ref.child(completed.id!!).setValue(completed)
                }
                //finish()
                //Toast.makeText(context, "Congrats! Activity completed", Toast.LENGTH_SHORT).show()
            }
            else {
                val userUID = FirebaseAuth.getInstance().currentUser?.uid
                val ref = userUID?.let {
                    FirebaseDatabase.getInstance().getReference("User").child(it).child("Activities")
                }
                val completed = dataSet[position]
                completed.completed = 0

                if (ref != null) {
                    ref.child(completed.id!!).setValue(completed)
                }
                //finish()
                //Toast.makeText(context, "Congrats! Activity completed", Toast.LENGTH_SHORT).show()
            }
        }

        fun setCompleted(index: Int) {
            val userUID = FirebaseAuth.getInstance().currentUser?.uid
            val ref = userUID?.let {
                FirebaseDatabase.getInstance().getReference("User").child(it).child("Activities")
            }
            val completed = dataSet[index]
            completed.completed = 1

            if (ref != null) {
                ref.child(completed.id!!).setValue(completed)
            }
            //finish()
            //Toast.makeText(context, "Congrats! Activity completed", Toast.LENGTH_SHORT).show()
        }

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val activity = dataSet[adapterPosition]
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onItemClick(activity)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(activity: Activity)
    }
}