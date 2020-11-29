package com.example.smartmanager.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.smartmanager.R
import com.example.smartmanager.model.Activity
import com.google.android.gms.common.util.ArrayUtils.newArrayList
import com.google.firebase.database.*

class HomeFragment : Fragment() {
    lateinit var ref: DatabaseReference
    lateinit var activityList: MutableList<Activity>
    lateinit var filteredActivity: MutableList<Activity>
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
        //listView.adapter = context?.let { MyCustomAdapter(it) }
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
        })

        fragmentContext = this.requireContext()
        ref = FirebaseDatabase.getInstance().getReference("Activity")
        activityList = mutableListOf()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {

                    activityList.clear()
                    for (h in p0.children) {
                        val recipe = h.getValue(Activity::class.java)
                        activityList.add(recipe!!)
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
        private val mContext : Context
        var myActivityList = newArrayList<Activity>()
        init{
            mContext = context
            myActivityList = activityList as java.util.ArrayList<Activity>?;
        }
        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val activityRow = layoutInflater.inflate(R.layout.activity_list_row, viewGroup, false)

            val activityName = activityRow.findViewById<TextView>(R.id.textView_name)
            activityName.text = myActivityList[position].activityName

            val activityStartTime = activityRow.findViewById<TextView>(R.id.textView_time)
            activityStartTime.text = myActivityList[position].startTime.toString()

            val activityLocation = activityRow.findViewById<TextView>(R.id.textView_location)
            activityLocation.text = myActivityList[position].color

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