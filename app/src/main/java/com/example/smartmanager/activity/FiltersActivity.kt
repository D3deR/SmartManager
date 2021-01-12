package com.example.smartmanager.activity


import android.os.Bundle
import android.view.MenuItem
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.smartmanager.R
import com.example.smartmanager.ui.gallery.GalleryFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FiltersActivity : AppCompatActivity() {

    val colorFilters = ArrayList<String>()
    var order: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filters)
        title = "Activity filters"

        //toolbar back arrow
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        //
        val blueCheckBox = findViewById<CheckBox>(R.id.checkBox_blue)
        val redCheckBox = findViewById<CheckBox>(R.id.checkBox_red)
        val yellowCheckBox = findViewById<CheckBox>(R.id.checkBox_yellow)

        val ascendingCheckBox = findViewById<CheckBox>(R.id.checkBox_low_to_high)
        val deschendingCheckBox = findViewById<CheckBox>(R.id.checkBox_high_to_low)

        val btnApply = findViewById<FloatingActionButton>(R.id.floatingActionButton_apply_filters)

        //makes other checkbox un-clickable
        ascendingCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            deschendingCheckBox.isClickable = !isChecked
        }
        deschendingCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            ascendingCheckBox.isClickable = !isChecked
        }

        btnApply.setOnClickListener {
            val args = Bundle()
            if (blueCheckBox.isChecked) {
                colorFilters.add("Blue")
                args.putString("blue", "true")
            } else {
                args.putString("blue", "false")
            }
            if (redCheckBox.isChecked) {
                colorFilters.add("Red")
                args.putString("red", "true")
            } else {
                args.putString("red", "false")
            }
            if (yellowCheckBox.isChecked) {
                colorFilters.add("Yellow")
                args.putString("yellow", "true")
            } else {
                args.putString("yellow", "false")
            }
            if (ascendingCheckBox.isChecked) {
                order = "Ascending"
                args.putString("order", "Ascending")
            } else if (deschendingCheckBox.isChecked) {
                order = "Descending"
                args.putString("order", "Descending")
            }
//            val intent = Intent(this, GalleryFragment::class.java)
//            intent.putExtra("extra", "hello")
//            startActivity(intent)

            //val args = Bundle()
//            args.putStringArrayList("color", colorFilters)
//            args.putString("red", "Red")
//            args.putString("order", "Ascending")

            val fragmentManager: FragmentManager = supportFragmentManager
            val dailyFragm = GalleryFragment()
            dailyFragm.arguments = args
            fragmentManager.beginTransaction().add(dailyFragm, "victory").commit()
            finish()
        }


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            finish() // close this activity and return to previous activity (if there is any)
        }
        return super.onOptionsItemSelected(item)
    }


}