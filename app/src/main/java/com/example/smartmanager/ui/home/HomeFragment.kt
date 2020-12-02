@file:Suppress("DEPRECATION")

package com.example.smartmanager.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.smartmanager.AddActivity
import com.example.smartmanager.R
import com.example.smartmanager.model.Activity
import com.google.android.gms.common.util.ArrayUtils.newArrayList
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment() {
    lateinit var ref: DatabaseReference
    lateinit var activityList: MutableList<Activity>
    lateinit var filteredActivity: MutableList<Activity>
    lateinit var adapter: MyCustomAdapter
    lateinit var fragmentContext : Context
    lateinit var date : String
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?

    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val listView: ListView = root.findViewById(R.id.activity_list_view)
        val calendar = root.findViewById<CalendarView>(R.id.calendarView)

        /*
        val text2:TextView = root.findViewById(R.id.text_slideshow)
        val fragment: Fragment? = fragmentManager?.findFragmentById(R.id.text_gallery)
        val fragment1: Fragment? = fragmentManager?.findFragmentById(R.id.text_slideshow)
         */

        //listView.adapter = context?.let { MyCustomAdapter(it) }
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
        })

        fragmentContext = this.requireContext()

        //add button action
        val addBtn : FloatingActionButton = root.findViewById(R.id.floating_action_button)
        addBtn.setOnClickListener{
            requireActivity().run{
                startActivity(Intent(this, AddActivity::class.java))
            }
        }

        //gets selected date from calendar
        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val stringDate = "$dayOfMonth/$month/$year"
            date = stringDate
            Toast.makeText(context,stringDate,Toast.LENGTH_LONG).show()
            filteredActivity = filterActivitiesByDate(date, activityList)
            //show activities with selected date
            adapter =
                MyCustomAdapter(
                    fragmentContext,
                    filteredActivity
                )
            listView.adapter = adapter
        }

        //load list of activity from firebase
        val userUID = FirebaseAuth.getInstance().currentUser?.uid
        ref = FirebaseDatabase.getInstance().getReference("User").child(userUID!!).child("Activities")

        activityList = mutableListOf()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    activityList.clear()
                    for (h in p0.children) {
                        val activity = h.getValue(Activity::class.java)
                        activityList.add(activity!!)
                    }
                    adapter =
                        MyCustomAdapter(
                            fragmentContext,
                            activityList
                        )
                    listView.adapter = adapter

                }
            }

        })
        return root
    }



    class MyCustomAdapter(context: Context, activityList: MutableList<Activity>) : BaseAdapter() {
        private val mContext : Context = context
        var myActivityList = newArrayList<Activity>()

        init{
            myActivityList = activityList as java.util.ArrayList<Activity>?
        }

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View? {
            val layoutInflater = LayoutInflater.from(mContext)

            if(myActivityList[position].color == "Blue"){
                val blueActivityRow = layoutInflater.inflate(R.layout.activity_list_row_blue, viewGroup, false)

                val activityName = blueActivityRow.findViewById<TextView>(R.id.textView_name)
                activityName.text = myActivityList[position].activityName

                val activityStartTime = blueActivityRow.findViewById<TextView>(R.id.textView_time)
                activityStartTime.text = myActivityList[position].startTime

                val activityDescription = blueActivityRow.findViewById<TextView>(R.id.textView_description)
                activityDescription.text = myActivityList[position].description

                val activityDate = blueActivityRow.findViewById<TextView>(R.id.textView_date)
                activityDate.text = myActivityList[position].date

                val activityColor = blueActivityRow.findViewById<TextView>(R.id.textView_color)
                activityColor.text = myActivityList[position].color

                val activityReminder = blueActivityRow.findViewById<TextView>(R.id.textView_reminder)
                if(myActivityList[position].reminder == 1){activityReminder.text = "reminder active"} else {activityReminder.text = "reminder inactive"}

                return blueActivityRow
            }
            else if(myActivityList[position].color == "Red"){
                val redActivityRow = layoutInflater.inflate(R.layout.activity_list_row_red, viewGroup, false)

                val activityName = redActivityRow.findViewById<TextView>(R.id.textView_name)
                activityName.text = myActivityList[position].activityName

                val activityStartTime = redActivityRow.findViewById<TextView>(R.id.textView_time)
                activityStartTime.text = myActivityList[position].startTime

                val activityDescription = redActivityRow.findViewById<TextView>(R.id.textView_description)
                activityDescription.text = myActivityList[position].description

                val activityDate = redActivityRow.findViewById<TextView>(R.id.textView_date)
                activityDate.text = myActivityList[position].date

                val activityColor = redActivityRow.findViewById<TextView>(R.id.textView_color)
                activityColor.text = myActivityList[position].color

                val activityReminder = redActivityRow.findViewById<TextView>(R.id.textView_reminder)
                if(myActivityList[position].reminder == 1){activityReminder.text = "reminder active"} else {activityReminder.text = "reminder inactive"}

                return redActivityRow
            }
            else if(myActivityList[position].color == "Yellow"){
                val yellowActivityRow = layoutInflater.inflate(R.layout.activity_list_row_yellow, viewGroup, false)

                val activityName = yellowActivityRow.findViewById<TextView>(R.id.textView_name)
                activityName.text = myActivityList[position].activityName

                val activityStartTime = yellowActivityRow.findViewById<TextView>(R.id.textView_time)
                activityStartTime.text = myActivityList[position].startTime

                val activityDescription = yellowActivityRow.findViewById<TextView>(R.id.textView_description)
                activityDescription.text = myActivityList[position].description

                val activityDate = yellowActivityRow.findViewById<TextView>(R.id.textView_date)
                activityDate.text = myActivityList[position].date

                val activityColor = yellowActivityRow.findViewById<TextView>(R.id.textView_color)
                activityColor.text = myActivityList[position].color

                val activityReminder = yellowActivityRow.findViewById<TextView>(R.id.textView_reminder)
                if(myActivityList[position].reminder == 1){activityReminder.text = "reminder active"} else {activityReminder.text = "reminder inactive"}

                return yellowActivityRow
            }else{
            val activityRow = layoutInflater.inflate(R.layout.activity_list_row, viewGroup, false)

            val activityName = activityRow.findViewById<TextView>(R.id.textView_name)
            activityName.text = myActivityList[position].activityName

            val activityStartTime = activityRow.findViewById<TextView>(R.id.textView_time)
            activityStartTime.text = myActivityList[position].startTime

            val activityDescription = activityRow.findViewById<TextView>(R.id.textView_description)
            activityDescription.text = myActivityList[position].description

            val activityDate = activityRow.findViewById<TextView>(R.id.textView_date)
            activityDate.text = myActivityList[position].date

            val activityColor = activityRow.findViewById<TextView>(R.id.textView_color)
            activityColor.text = myActivityList[position].color

            val activityReminder = activityRow.findViewById<TextView>(R.id.textView_reminder)
            if(myActivityList[position].reminder == 1){activityReminder.text = "reminder active"} else {activityReminder.text = "reminder inactive"}

            return activityRow
            }
        }

        override fun getItem(position: Int): Any {
            return "Test string"
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return myActivityList.size
        }

    }

    //filters the given list by date
    fun filterActivitiesByDate(date :String, list : MutableList<Activity>) : MutableList<Activity>{
        val filteredActivities: MutableList<Activity> = mutableListOf()
        for(activity in list){
            if (activity.date == date){
                filteredActivities.add(activity)
            }
        }
        return filteredActivities
    }
}