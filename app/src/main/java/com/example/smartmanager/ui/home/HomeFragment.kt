@file:Suppress("DEPRECATION")

package com.example.smartmanager.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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
    //lateinit var filteredActivity: MutableList<Activity>
    lateinit var adapter: MyCustomAdapter
    lateinit var fragmentContext : Context
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
}