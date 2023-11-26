package com.example.customcalendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
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

        val weekBtn = findViewById<ImageView>(R.id.weekBtn)
        weekBtn.setOnClickListener {
            val eventActivity = Intent(this,RowActivity::class.java)
            startActivity(eventActivity)
        }

        // Load data...
        val ids: MutableList<Int> = mutableListOf()
        val titles: MutableList<String> = mutableListOf()
        val contents: MutableList<String> = mutableListOf()

        val db = MyDb(this, null)
        val cursor = db.getEvent()

//        Log.d("TTT", cursor!!.count.toString())

        cursor!!.moveToFirst()
        if(cursor!!.count > 0) {
            ids.add(cursor.getInt(cursor.getColumnIndex(MyDb.ID_COL)))
            titles.add(cursor.getString(cursor.getColumnIndex(MyDb.DATE_COL)))
            contents.add(cursor.getString(cursor.getColumnIndex(MyDb.CONTENT_COL)))
        }

        while(cursor.moveToNext()){
            ids.add(cursor.getInt(cursor.getColumnIndex(MyDb.ID_COL)))
            titles.add(cursor.getString(cursor.getColumnIndex(MyDb.DATE_COL)))
            contents.add(cursor.getString(cursor.getColumnIndex(MyDb.CONTENT_COL)))
        }
        cursor.close()

        var planManager = LinearLayoutManager(this)
        var planAdapter = AdapterPlan(ids, titles, contents)
        val rv2 = findViewById<RecyclerView>(R.id.recyclerViewPlan)

        rv2.apply {
            adapter = planAdapter
            layoutManager = planManager
        }

        val btnEvent = findViewById<ImageView>(R.id.btnEvent)
        btnEvent.setOnClickListener {
            // open event activity
            val eventActivity = Intent(this,EventActivity::class.java)
//            eventActivity.putExtra("date", MyApp.preferences.getLong("selected_date",-1))
            startActivity(eventActivity)
        }

        // set selected_date to default value
        MyApp.preferences.setLong("selected_date",Date().time)
        MyApp.preferences.setLong("cal_mode", 0)

    }
}