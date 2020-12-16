package com.example.smartmanager.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.smartmanager.R
import com.example.smartmanager.model.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SlideshowFragment : Fragment() {
    lateinit var ref: DatabaseReference
    lateinit var activityList: MutableList<Activity>
    private lateinit var slideshowViewModel: SlideshowViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
        val textView: TextView = root.findViewById(R.id.text_slideshow)
        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

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

                    val size = activityList.size
                    val completed = countCompleted(activityList)
                    Toast.makeText(context,"activity count : $size  completed activities : $completed" , Toast.LENGTH_SHORT).show()
                }
            }

        })
        return root
    }
    fun countCompleted(activityList : MutableList<Activity>): Int {
        var completed = 0
        for(activity in activityList){
            if(activity.completed == 1){
                completed++
            }
        }
        return completed
    }
}