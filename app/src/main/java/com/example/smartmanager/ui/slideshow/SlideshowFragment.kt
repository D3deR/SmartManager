package com.example.smartmanager.ui.slideshow

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.smartmanager.R
import com.example.smartmanager.activity.MonthlyStatisticsActivity

import com.example.smartmanager.activity.OnSwipeTouchListener
import com.example.smartmanager.model.Activity
import com.example.smartmanager.ui.home.HomeFragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.database.DatabaseReference
import java.util.*



    class SlideshowFragment : Fragment() {
        lateinit var ref: DatabaseReference
        lateinit var activityList: MutableList<Activity>
        private var barDataSet: BarDataSet? = null
        lateinit var barChart: BarChart
        var barData: BarData? = null

        @SuppressLint("ClickableViewAccessibility")
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
            val context: Context = inflater.context
            barChart = root.findViewById(R.id.barChart)
            (activity as AppCompatActivity).supportActionBar?.title = "Statistics"
            barChart.description = Description()
            barChart.setOnTouchListener(object : OnSwipeTouchListener(context) {
                @SuppressLint("ResourceType")
                override fun onSwipeRight() {
                    barChart.setNoDataText("")
                    barChart.clear();
                    val fragmentManager: FragmentManager? = fragmentManager
                    val fragmentTransaction: FragmentTransaction? = fragmentManager
                        ?.beginTransaction()
                        ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    val fragment3 = MonthlyStatisticsActivity()
                    fragmentTransaction?.replace(R.id.child_fragment_container, fragment3)
                    fragmentTransaction?.commit()
                    Toast.makeText(context, "Monthly statistics", Toast.LENGTH_SHORT).show()
                }
            })

            activityList = HomeFragment.activityList
            barDataSet = BarDataSet(getData(), "Daily View")
            barData = BarData(barDataSet)
            barDataSet!!.barBorderWidth = 3f
            val barData = BarData(barDataSet)
            val xAxis = barChart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            val months =
                arrayOf("Luni", "Marti", "Miercuri", "Joi", "Vineri", "Sambata", "Duminica")
            val formatter = IndexAxisValueFormatter(months)
            xAxis.granularity = 1f
            xAxis.valueFormatter = formatter
            barChart.data = barData
            barChart.animateXY(5000, 5000)
            return root
        }

        private fun returnNextDate(date: String): String {
            when (date) {
                "01" -> return "02"
                "02" -> return "03"
                "03" -> return "04"
                "04" -> return "05"
                "05" -> return "06"
                "06" -> return "07"
                "07" -> return "08"
                "08" -> return "09"
                "09" -> return "10"
                "10" -> return "11"
                "11" -> return "12"
                "12" -> return "13"
                "13" -> return "14"
                "14" -> return "15"
                "15" -> return "16"
                "16" -> return "17"
                "17" -> return "18"
                "18" -> return "19"
                "19" -> return "20"
                "20" -> return "21"
                "21" -> return "22"
                "22" -> return "23"
                "23" -> return "24"
                "24" -> return "25"
                "25" -> return "26"
                "26" -> return "27"
                "27" -> return "28"
                "28" -> return "29"
                "29" -> return "30"
                "30" -> return "31"

            }
            return "Ce"
        }

        private fun trimActivities(Date: String): MutableList<Activity> {
            val myList = mutableListOf<Activity>()
            for (activity in activityList) {
                val myVal = activity.date.toString();
                if (myVal == Date) myList.add(activity)
            }
            return myList
        }

        private fun calculateDailyPercent(mutableList: MutableList<Activity>): Float {
            var completed = 0
            val activityNumber = mutableList.size
            for (activity in mutableList) {
                if (activity.completed == 1) completed++;
            }
            if (activityNumber == 0) {
                return 0f
            }
            return (completed.toFloat() / activityNumber.toFloat()) * 100
        }


        private fun addBarData(day: Int, date: String): BarEntry {
            val trimmedActivities = trimActivities(date)
            val calculatedDailyPercent = calculateDailyPercent(trimmedActivities)
            return (BarEntry(day.toFloat(), calculatedDailyPercent))
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun getData(): MutableList<BarEntry>? {
            val cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
            val mondayOfWeek = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"))
            mondayOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            val date = mondayOfWeek.time.toString().substring(8, 10)
            val month = cal.get(Calendar.MONTH)
            val year = cal.get(Calendar.YEAR)
            val entries: ArrayList<BarEntry> = arrayListOf()
            var finalDate = date.plus("/").plus(month.toString().plus("/").plus(year.toString()))
            entries.add(addBarData(0, finalDate))
            for (i in 1..6) {
                val firstOfDate = finalDate.substring(0, 2)
                val newFirstOfDate = returnNextDate(firstOfDate)
                finalDate = finalDate.replaceFirst(firstOfDate, newFirstOfDate)
                entries.add(addBarData(i, finalDate))

            }
            return entries
        }
    }