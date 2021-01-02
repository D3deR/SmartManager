package com.example.smartmanager.activity


import android.R.attr.fragment
import android.R.attr.key
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.example.smartmanager.R
import com.example.smartmanager.ui.gallery.GalleryFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FiltersActivity : AppCompatActivity() {

    val colorFilters = ArrayList<String>()
    var order : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filters)
        title = "Activity filters"

        //toolbar back arrow
        if (supportActionBar != null){
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

        btnApply.setOnClickListener{
//            if(blueCheckBox.isChecked){
//                colorFilters.add("Blue")
//            }
//            if(redCheckBox.isChecked){
//                colorFilters.add("Red")
//            }
//            if(yellowCheckBox.isChecked){
//                colorFilters.add("Yellow")
//            }
//            if(ascendingCheckBox.isChecked){
//                order = "Ascending"
//            }else if(deschendingCheckBox.isChecked){
//                order = "Descending"
//            }
//            if(ascendingCheckBox.isChecked && deschendingCheckBox.isChecked ){
//                Toast.makeText(this, "You can only select one order!", Toast.LENGTH_SHORT).show()
//            }else{
//                val changePage = Intent(this, GalleryFragment::class.java)
//                changePage.putExtra("colors", colorFilters)
//                changePage.putExtra("order", order)
//                startActivity(changePage)
//            }

//            if(ascendingCheckBox.isChecked){
//                val changePage = Intent(this, GalleryFragment::class.java)
//                changePage.putExtra("order", "Ascending")
//                startActivity(changePage)
//            }

//            val intent = Intent(this, GalleryFragment::class.java)
//            intent.putExtra("extra", "hello")
//            startActivity(intent)

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