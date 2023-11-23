package com.example.customcalendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.Date

class RowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_row)

        val temp = findViewById<TextView>(R.id.rowTextView)
        val weekListManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val weekListAdapter = AdapterWeeks(temp)

        val rv = findViewById<RecyclerView>(R.id.rcRowView)
        rv.apply {
            layoutManager = weekListManager
            adapter = weekListAdapter
            scrollToPosition(Int.MAX_VALUE/2)
        }
        val snap = PagerSnapHelper()
        snap.attachToRecyclerView(rv)

        val monthBtn = findViewById<ImageView>(R.id.monthBtn)
        monthBtn.setOnClickListener {
            val eventActivity = Intent(this,MainActivity::class.java)
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
        val rv2 = findViewById<RecyclerView>(R.id.rcRowViewPlan)

        rv2.apply {
            adapter = planAdapter
            layoutManager = planManager
        }

        val btnEvent = findViewById<ImageView>(R.id.btnRowEvent)
        btnEvent.setOnClickListener {
            // open event activity
            val eventActivity = Intent(this,EventActivity::class.java)
            startActivity(eventActivity)
        }

        // set selected_date to default value
        MyApp.preferences.setLong("selected_date", Date().time)

    }
}