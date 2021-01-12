package com.example.smartmanager.ui.gallery

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmanager.R
import com.example.smartmanager.activity.DetailActivity
import com.example.smartmanager.activity.FiltersActivity
import com.example.smartmanager.adapters.GalleryFragmentAdapter
import com.example.smartmanager.model.Activity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


class GalleryFragment : Fragment(), GalleryFragmentAdapter.OnItemClickListenerGallery {
    private lateinit var galleryViewModel: GalleryViewModel
    lateinit var ref: DatabaseReference
    lateinit var activityList: MutableList<Activity>
    lateinit var filteredActivity: MutableList<Activity>
    lateinit var filtersApplied: MutableList<Activity>
    lateinit var date: String
    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#8bc34a"))
    lateinit var fragmentContext: Context

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        val textView: TextView = root.findViewById(R.id.text_no_activities)

        (activity as AppCompatActivity).supportActionBar?.title = "Daily Activities"

        //filter button action
        val filterBtn: FloatingActionButton = root.findViewById(R.id.floatingActionButton_filter)
        filterBtn.setOnClickListener {
            requireActivity().run {
                startActivity(Intent(this, FiltersActivity::class.java))
            }
        }


        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerView_galleryFragm)
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        val userUID = FirebaseAuth.getInstance().currentUser?.uid
        ref =
            FirebaseDatabase.getInstance().getReference("User").child(userUID!!).child("Activities")

        activityList = mutableListOf()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //TODO("Not yet implemented")
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    activityList.clear()
                    for (h in p0.children) {
                        val activity = h.getValue(Activity::class.java)
                        activityList.add(activity!!)
                    }
                }
                filteredActivity = filterList(activityList)
                if (filteredActivity.size > 0) {
                    textView.visibility = View.INVISIBLE //GONE
                    if (arguments != null) {
                        filtersApplied = applyFilters(filteredActivity)
                        filteredActivity.clear()
                        filteredActivity = filtersApplied

                        val newRecyclerView: RecyclerView = root.findViewById(R.id.recyclerView_galleryFragm)
                        recyclerView.layoutManager = LinearLayoutManager(context)
                        recyclerView.adapter = GalleryFragmentAdapter(filteredActivity,this@GalleryFragment)

//                        recyclerView.adapter =
//                            GalleryFragmentAdapter(filtersApplied, this@GalleryFragment)
                        if (recyclerView.adapter != null) {
                            recyclerView.adapter!!.notifyDataSetChanged()
                        }
                    } else {
                        recyclerView.adapter =
                            GalleryFragmentAdapter(filteredActivity, this@GalleryFragment)
                    }
                }

            }
        })

        //filter elements
//        if(arguments != null){
////            val blueActivities: MutableList<Activity>
////            val redActivities: MutableList<Activity>
////            val yellowActivities: MutableList<Activity>
////            var allFilters : MutableList<Activity> //=  mutableListOf<Activity>()
//
//            val mArgs = arguments
//            val red = mArgs?.getString("red")
//            val blue = mArgs?.getString("blue")
//            val yellow = mArgs?.getString("yellow")
//            val order = mArgs?.getString("order")
//            Toast.makeText(context, "Red : $red", Toast.LENGTH_SHORT).show()
//            Toast.makeText(context, "Blue : $blue", Toast.LENGTH_SHORT).show()
//            Toast.makeText(context, "Yellow : $yellow", Toast.LENGTH_SHORT).show()
//            Toast.makeText(context, order, Toast.LENGTH_SHORT).show()
//
//            if (blue == "true") {
//                Toast.makeText(context, "Blue : $red", Toast.LENGTH_SHORT).show()
//                //blueActivities = filterBlue(filteredActivity)
//               //allFilters = mergeLists(blueActivities, blueActivities)
//            }
//            if (red == "true") {
//                Toast.makeText(context, "Red : $blue", Toast.LENGTH_SHORT).show()
//
//            }
//            if (yellow == "true") {
//                Toast.makeText(context, "Yellow : $yellow", Toast.LENGTH_SHORT).show()
//
//            }
//
//        }

        //filter elements
//        if (requireActivity().intent.hasExtra("colors")) {
//            val colorFilters: ArrayList<String> =
//                requireActivity().intent.extras!!.getStringArrayList("colors") as ArrayList<String>
//            if (colorFilters.contains("Blue")) {
//                val blue: MutableList<Activity> = filterBlue(activityList)
//                filtersApplied = (filtersApplied + blue) as MutableList<Activity>
//            }
//            if (colorFilters.contains("Red")) {
//                val red: MutableList<Activity> = filterRed(activityList)
//                filtersApplied = (filtersApplied + red) as MutableList<Activity>
//            }
//            if (colorFilters.contains("Yellow")) {
//                val yellow: MutableList<Activity> = filterYellow(activityList)
//                filtersApplied = (filtersApplied + yellow) as MutableList<Activity>
//            }
//        }


        //order elements
//        if (requireActivity().intent.hasExtra("order")) {
//            if (filtersApplied.size != 0) {
//                val oredrFilter: String? = requireActivity().intent.extras!!.getString("order")
//                if (oredrFilter == "Ascending") {
//                    sortByPriorityAscending(filtersApplied)
//                } else if (oredrFilter == "Descending") {
//                    sortByPriorityDescending(filtersApplied)
//                }
//                recyclerView.adapter =
//                    GalleryFragmentAdapter(filtersApplied, this@GalleryFragment)
//            } else {
//                val oredrFilter: String? = requireActivity().intent.extras!!.getString("order")
//                if (oredrFilter == "Ascending") {
//                    sortByPriorityAscending(activityList)
//                } else if (oredrFilter == "Descending") {
//                    sortByPriorityDescending(activityList)
//                }
//                recyclerView.adapter =
//                    GalleryFragmentAdapter(activityList, this@GalleryFragment)
//            }
//
//        }

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
                    if (filteredActivity[viewHolder.adapterPosition].completed == 0) {
                        setCompleted(viewHolder.adapterPosition)
                    } else {
                        Toast.makeText(context, "Activity is already completed", Toast.LENGTH_SHORT)
                            .show()
                    }

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
                    if (filteredActivity[viewHolder.adapterPosition].completed == 0) {
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
            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        return root
    }

    fun setCompleted(index: Int) {
        val userUID = FirebaseAuth.getInstance().currentUser?.uid
        val ref = userUID?.let {
            FirebaseDatabase.getInstance().getReference("User").child(it).child("Activities")
        }
        val completed = filteredActivity[index]
        completed.completed = 1

        if (ref != null) {
            ref.child(completed.id!!).setValue(completed)
        }
        //finish()
        Toast.makeText(context, "Congrats! Activity completed", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun filterList(list: MutableList<Activity>): MutableList<Activity> {
        val filteredList = mutableListOf<Activity>()
        for (activity in list) {
            if (compareDate(activity)) {
                filteredList.add(activity)
            }
        }
        return filteredList
    }

    fun sortByPriorityAscending(list: MutableList<Activity>): MutableList<Activity> {
        return list.sortedBy { it.priority } as MutableList<Activity>
    }

    fun sortByPriorityDescending(list: MutableList<Activity>): MutableList<Activity> {
        return list.sortedByDescending { it.priority } as MutableList<Activity>
    }

    fun filterBlue(list: MutableList<Activity>): MutableList<Activity> {
        val blueList = mutableListOf<Activity>()
        for (activity: Activity in list) {
            if (activity.color == "Blue") {
                blueList.add(activity)
            }
        }
        return blueList
    }

    fun filterRed(list: MutableList<Activity>): MutableList<Activity> {
        val redList = mutableListOf<Activity>()
        for (activity: Activity in list) {
            if (activity.color == "Red") {
                redList.add(activity)
            }
        }
        return redList
    }

    fun filterYellow(list: MutableList<Activity>): MutableList<Activity> {
        val yellowList = mutableListOf<Activity>()
        for (activity: Activity in list) {
            if (activity.color == "Yellow") {
                yellowList.add(activity)
            }
        }
        return yellowList
    }

    fun applyFilters(list: MutableList<Activity>): MutableList<Activity> {
        val blueActivities: MutableList<Activity>
        val redActivities: MutableList<Activity>
        val yellowActivities: MutableList<Activity>
        var allColors = mutableListOf<Activity>()
        val mArgs = arguments
        val red = mArgs?.getString("red")
        val blue = mArgs?.getString("blue")
        val yellow = mArgs?.getString("yellow")
        val order = mArgs?.getString("order")

        if (blue == "true") {
            blueActivities = filterBlue(list)
            allColors = mergeLists(allColors, blueActivities)
        }
        if (red == "true") {
            redActivities = filterRed(list)
            allColors = mergeLists(allColors, redActivities)
        }
        if (yellow == "true") {
            yellowActivities = filterYellow(list)
            allColors = mergeLists(allColors, yellowActivities)
        }
        return allColors
    }

    fun compareDate(activity: Activity): Boolean {
        val sdf2 = SimpleDateFormat("dd/MM/yyyy")
//        var date = Calendar.getInstance().time

        val currentDate = sdf2.format(Calendar.getInstance().time)
        if (activity.date == currentDate) {
            return true
        }
        return false
    }

    private fun mergeLists(
        list1: MutableList<Activity>,
        list2: MutableList<Activity>
    ): MutableList<Activity> {
        for (elem in list2) {
            list1.add(elem)
        }
        return list1
    }

    override fun onItemClick(activity: Activity) {
        val changePage = Intent(this.context, DetailActivity::class.java)
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