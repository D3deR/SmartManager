package com.example.smartmanager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmanager.R
import com.example.smartmanager.model.Activity
import com.example.smartmanager.ui.home.HomeFragment
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