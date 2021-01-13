package com.example.smartmanager.activity



import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.smartmanager.R
import com.example.smartmanager.model.Activity
import com.example.smartmanager.ui.home.HomeFragment
import com.example.smartmanager.ui.slideshow.SlideshowFragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.database.DatabaseReference
import java.util.*


class MonthlyStatisticsActivity : Fragment() {
    private var barDataSet: BarDataSet? = null
    lateinit var barChart: BarChart
    var barData: BarData? = null
    lateinit var activityList:MutableList<Activity>

    @SuppressLint("ClickableViewAccessibility", "ResourceType")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):  View? {
        val root = inflater.inflate(R.layout.test, container, false)
        barChart = root.findViewById(R.id.barChart)
        context?.let {
            barChart.setOnTouchListener(object:OnSwipeTouchListener(it) {
                @SuppressLint("ResourceType")
                override fun onSwipeLeft() {
                    barChart.setNoDataText("")
                    barChart.clear()
                    val fragmentManager: FragmentManager? = fragmentManager
                    val fragmentTransaction: FragmentTransaction? = fragmentManager
                        ?.beginTransaction()?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    val fragment3 = SlideshowFragment()
                    fragmentTransaction?.replace(R.id.child_fragment_container, fragment3)
                    fragmentTransaction?.commit()
                    Toast.makeText(context, "Daily statistics", Toast.LENGTH_SHORT).show()
                }
            })
        }
        activityList = HomeFragment.activityList
        barDataSet = BarDataSet(getData() ,"Monthly View" )
        barData = BarData(barDataSet)
        barDataSet!!.barBorderWidth = 3f
        val barData = BarData(barDataSet)
        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        val months =
            arrayOf("Ianuarie", "Februarie", "Martie",
                "Aprilie", "Mai", "Iunie",
                "Iulie", "August", "Septembrie",
                "Octombrie", "Noiembrie", "Decembrie")
        val formatter = IndexAxisValueFormatter(months)
        xAxis.granularity = 1f
        xAxis.valueFormatter = formatter
        barChart.data = barData
        barChart.animateXY(5000, 5000)
        barChart.invalidate()
        return root

    }

    private fun trimActivities( Date: String): MutableList<Activity> {
        val myList = mutableListOf<Activity>()
        for(activity in activityList)
        {
            val myVal = activity.date.toString();
            if(myVal== Date) myList.add(activity)
        }
        return myList
    }

    private fun calculateDailyPercent(mutableList: MutableList<Activity>): Float
    {
        var completed = 0
        val activityNumber = mutableList.size
        for(activity in mutableList)
        {
            if(activity.completed == 1) completed++;
        }
        if(activityNumber == 0)
        {
            return 0f
        }
        return completed.toFloat()
    }

    private fun tasksPerMonth(date:String):Int
    {
        return trimActivities(date).size
    }

    private fun calculateMonth(date:String):Float
    {
        val trimmedActivities = trimActivities(date)
        val calculatedDailyPercent = calculateDailyPercent(trimmedActivities)
        return calculatedDailyPercent
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getData(): MutableList<BarEntry>? {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        val monthsWith30Days = arrayOf(3, 5, 8, 10)
        val year = cal.get(Calendar.YEAR)
        val entries: ArrayList<BarEntry> = arrayListOf()
        for (i in 0..11) {
            var totalPerMonth = 0f
            var numberOfActivities = 0
            for (j: Int in 1..31) {
                if (i == 30 && monthsWith30Days.contains(j)) break;
                if (i == 28 && j == 1) break;
                val finalDate =
                    j.toString().plus("/")
                        .plus(i.toString()
                            .plus("/")
                        .plus(year.toString()))
                Log.d(TAG,finalDate)
                totalPerMonth += calculateMonth(finalDate)
                numberOfActivities +=tasksPerMonth(finalDate)
            }
            val calculatedData = (totalPerMonth/numberOfActivities)*100
            if(calculatedData!= 0f) {
                entries.add(BarEntry(i.toFloat(), calculatedData))
            }else entries.add(BarEntry(i.toFloat(),0f))

        }
        return entries;
    }

}