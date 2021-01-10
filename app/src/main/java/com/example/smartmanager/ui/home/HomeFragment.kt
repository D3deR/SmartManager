@file:Suppress("DEPRECATION")

package com.example.smartmanager.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmanager.activity.AddActivity
import com.example.smartmanager.R
import com.example.smartmanager.activity.MainDetailActivity
import com.example.smartmanager.adapters.ActivityAdapter
import com.example.smartmanager.model.Activity
import com.google.android.gms.common.util.ArrayUtils.newArrayList
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment(), ActivityAdapter.OnItemClickListener {
    lateinit var ref: DatabaseReference
    lateinit var activityList: MutableList<Activity>
    lateinit var filteredActivity: MutableList<Activity>
    lateinit var adapter: MyCustomAdapter
    lateinit var fragmentContext : Context
    lateinit var date : String
    private lateinit var homeViewModel: HomeViewModel
    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#B30000"))

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?

    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        //val listView: ListView = root.findViewById(R.id.activity_list_view)
        val recyclerView : RecyclerView = root.findViewById(R.id.recyclerView_activity)
        val calendar = root.findViewById<CalendarView>(R.id.calendarView)

        recyclerView.layoutManager = LinearLayoutManager(this.context)
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
            recyclerView.adapter = ActivityAdapter(filteredActivity, this)
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
                    recyclerView.adapter = ActivityAdapter(activityList,this@HomeFragment)
                }
            }

        })

        //Swipe to Delete
        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val activitiesAsString = ArrayList<String?>()
                    for (item in activityList) {
                        val title = item.activityName
                        activitiesAsString += title
                    }
                    val alert = context?.let { AlertDialog.Builder(it) } //val alert = AlertDialog.Builder(context)
                    alert!!.setCancelable(false)
                    alert.setTitle(
                        resources.getText(R.string.are_you_sure)
                            .toString() + activitiesAsString[viewHolder.adapterPosition] + resources.getText(
                            R.string.questionMark
                        ).toString()
                    )
                    alert.setPositiveButton(
                        resources.getText(R.string.yes).toString()
                    ) { _: DialogInterface, _: Int ->
                        removeItem(viewHolder.adapterPosition)
                    }

                    alert.setNegativeButton(
                        resources.getText(R.string.no).toString()
                    ) { _: DialogInterface, _: Int ->
                        //adapter.notifyDataSetChanged()
                        recyclerView.adapter?.notifyDataSetChanged()
                    }
                    alert.show()
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    val itemView = viewHolder.itemView
                    if (dX > 0) {
                        swipeBackground.setBounds(
                            itemView.left,
                            itemView.top,
                            dX.toInt(),
                            itemView.bottom
                        )
                    } else {
                        swipeBackground.setBounds(
                            itemView.right + dX.toInt(),
                            itemView.top,
                            itemView.right,
                            itemView.bottom
                        )
                    }
                    swipeBackground.draw(c)

                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }


        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return root
    }


    //nu mai folosim asta
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
                if(myActivityList[position].reminder == 1){activityReminder.text = R.string.reminder_active.toString()
                } else {activityReminder.text = R.string.reminder_inactive.toString()}

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
                if(myActivityList[position].reminder == 1){activityReminder.text = R.string.reminder_active.toString()} else {activityReminder.text = R.string.reminder_inactive.toString()}

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
                if(myActivityList[position].reminder == 1){activityReminder.text = R.string.reminder_active.toString()} else {activityReminder.text = R.string.reminder_inactive.toString()}

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
            if(myActivityList[position].reminder == 1){activityReminder.text = R.string.reminder_active.toString()} else {activityReminder.text = R.string.reminder_inactive.toString()}

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

    //delete an activity from Firebase
    fun removeItem(index: Int){
        val userUID = FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("User").child(userUID!!).child("Activities")
        ref.child(activityList[index].id!!).removeValue()
        Toast.makeText(context,"removed successfully",Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(activity: Activity) {
        //val changePage = Intent(this.context, EditActivity::class.java)
        val changePage = Intent(this.context, MainDetailActivity::class.java)
        changePage.putExtra("aId", activity.id)
        changePage.putExtra("aName", activity.activityName)
        changePage.putExtra("aDescription", activity.description)
        changePage.putExtra("aStartTime", activity.startTime)
        changePage.putExtra("aDate", activity.date)
        changePage.putExtra("aColor", activity.color)
        changePage.putExtra("aReminder", activity.reminder)
        changePage.putExtra("aCompleted", activity.completed)
        changePage.putExtra("aPriority", activity.priority)
        startActivity(changePage)
    }

}