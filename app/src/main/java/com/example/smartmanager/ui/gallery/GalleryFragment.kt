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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmanager.R
import com.example.smartmanager.activity.DetailActivity
import com.example.smartmanager.adapters.GalleryFragmentAdapter
import com.example.smartmanager.model.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


class GalleryFragment : Fragment(), GalleryFragmentAdapter.OnItemClickListenerGallery {
    private lateinit var galleryViewModel: GalleryViewModel
    lateinit var ref: DatabaseReference
    lateinit var activityList: MutableList<Activity>
    lateinit var filteredActivity: MutableList<Activity>
    lateinit var date: String
    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#8bc34a"))
    lateinit var fragmentContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        val textView: TextView = root.findViewById(R.id.text_no_activities)


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
                    recyclerView.adapter =
                        GalleryFragmentAdapter(filteredActivity, this@GalleryFragment)
                }

            }
        })

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
                    setCompleted(viewHolder.adapterPosition)
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

    fun compareDate(activity: Activity): Boolean {
        val sdf2 = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = sdf2.format(Calendar.getInstance().time)
        if (activity.date == currentDate) {
            return true
        }
        return false
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
        startActivity(changePage)
    }
}