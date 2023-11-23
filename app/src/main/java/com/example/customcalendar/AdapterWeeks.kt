package com.example.customcalendar

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class AdapterWeeks(val txt: TextView):  RecyclerView.Adapter<AdapterWeeks.ListAdapter>(){
    private var prevItem: Int = -1

    val center = Int.MAX_VALUE / 2
    private var calendar = Calendar.getInstance()

    class ListAdapter(val layout: View): RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter {
        return ListAdapter(LayoutInflater.from(parent.context).inflate(R.layout.list_item_weeks, parent, false))
    }

    override fun onBindViewHolder(holder: ListAdapter, position: Int) {
        calendar.time = Date()
//        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.add(Calendar.DAY_OF_MONTH, position - center)
//
        val tempYear = calendar.get(Calendar.YEAR)
        val tempMonth = calendar.get(Calendar.MONTH)  // start from 0
        val tempMonth2 = tempMonth + 1
        val tempDay = calendar.get(Calendar.DAY_OF_MONTH)
        val tempWeek = calendar.get(Calendar.DAY_OF_WEEK)
//
//        calendar.add(Calendar.DAY_OF_MONTH, (1-calendar.get(Calendar.DAY_OF_WEEK)) )


        val tv3 = holder.layout.findViewById<TextView>(R.id.textImg3)
        tv3.text = tempYear.toString()
        txt.text = tempYear.toString()

        val tv2 = holder.layout.findViewById<TextView>(R.id.textImg2)
        tv2.text = "${tempMonth2.toString()} 월"

        val tv = holder.layout.findViewById<TextView>(R.id.textImg)
//        tv.text = calendar.time.toString()
        tv.text = tempDay.toString()

        val simpleDateFormat = SimpleDateFormat("EEE")
        val dayOfTheWeek = simpleDateFormat.format(calendar.time)

        val tv4 = holder.layout.findViewById<TextView>(R.id.textImg4)
        tv4.text = dayOfTheWeek.toString()

        if(prevItem == position.toInt())
            holder.layout.setBackgroundResource(R.drawable.drawborder)
        else
            holder.layout.setBackgroundColor(Color.WHITE)

        holder.layout.setOnClickListener {
            MyApp.preferences.setLong("selected_date", calendar.time.time)



            Toast.makeText(it.context, calendar.time.time.toString(), Toast.LENGTH_SHORT).show()

            prevItem = position.toInt()

            notifyDataSetChanged()
//            notifyItemChanged(position)
        }

    }

    override fun onViewRecycled(holder: ListAdapter) {
        super.onViewRecycled(holder)
//        Log.d("Debug", center.toString() )
        txt.text =calendar.get(Calendar.YEAR).toString()
    }
    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }
}