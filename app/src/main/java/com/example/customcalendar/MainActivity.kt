package com.example.customcalendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val monthListManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val monthListAdapter = AdapterMonth()

        val rv = findViewById<RecyclerView>(R.id.recyclerView)
        rv.apply {
            layoutManager = monthListManager
            adapter = monthListAdapter
            scrollToPosition(Int.MAX_VALUE/2)
        }
        val snap = PagerSnapHelper()
        snap.attachToRecyclerView(rv)


        // Load data...
        val titles: MutableList<String> = mutableListOf()
        val contents: MutableList<String> = mutableListOf()

        val db = MyDb(this, null)
        val cursor = db.getEvent()

//        Log.d("TTT", cursor!!.count.toString())

        cursor!!.moveToFirst()
        if(cursor!!.count > 0) {
            titles.add(cursor.getString(cursor.getColumnIndex(MyDb.DATE_COL)))
            contents.add(cursor.getString(cursor.getColumnIndex(MyDb.CONTENT_COL)))
        }

        while(cursor.moveToNext()){
            titles.add(cursor.getString(cursor.getColumnIndex(MyDb.DATE_COL)))
            contents.add(cursor.getString(cursor.getColumnIndex(MyDb.CONTENT_COL)))
        }
        cursor.close()

        var planManager = LinearLayoutManager(this)
        var planAdapter = AdapterPlan(titles, contents)
        val rv2 = findViewById<RecyclerView>(R.id.recyclerViewPlan)

        rv2.apply {
            adapter = planAdapter
            layoutManager = planManager
        }

        val btnEvent = findViewById<Button>(R.id.btnEvent)
        btnEvent.setOnClickListener {
            // open event activity
            val eventActivity = Intent(this,EventActivity::class.java)
            eventActivity.putExtra("date", MyApp.preferences.getLong("selected_date",-1))
            startActivity(eventActivity)
        }

        // set selected_date to default value
        MyApp.preferences.setLong("selected_date",Date().time)

    }
}