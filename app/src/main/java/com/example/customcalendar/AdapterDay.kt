package com.example.customcalendar

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.util.Date


class AdapterDay(val tempMonth:Int, val dayList: MutableList<Date>): RecyclerView.Adapter<AdapterDay.DayView>() {
    private var dateItem: Date = Date()
    private var prevItem: Int = -1
    val ROW = 6
    inner class DayView(val layout: View): RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayView {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_day, parent, false)
        return DayView(view)
    }

    override fun onBindViewHolder(holder: DayView, position: Int) {

        if(prevItem == position.toInt())
            holder.layout.setBackgroundColor(Color.WHITE)

        if(dateItem == dayList[position]) {
//            holder.layout.setBackgroundColor(Color.YELLOW)
            holder.layout.setBackgroundResource(R.drawable.drawborder)
            prevItem = position.toInt()
        } else {
//            holder.layout.setBackgroundColor(Color.WHITE)
            holder.layout.setBackgroundResource(0)
        }

        if(tempMonth == dayList[position].month) {
            holder.layout.setOnClickListener {
                MyApp.preferences.setLong("selected_date",dayList[position].time)
                dateItem = dayList[position]
                //            notifyDataSetChanged()

                //            cnt++
                //            if(cnt % 2 == 0) {
                //                MyApp.preferences.setString("Test",cnt.toString())
                //                Toast.makeText(holder.layout.context, "${dayList[position]}", Toast.LENGTH_SHORT).show()
                //            } else {
                //                val ttt = MyApp.preferences.getString("Test", "Okay2")
                //                Toast.makeText(holder.layout.context, ttt, Toast.LENGTH_SHORT).show()
                //            }

//                notifyDataSetChanged()
                notifyItemChanged(position)
                if (prevItem >= 0)
                    notifyItemChanged(prevItem)
                //            notifyItemRangeChanged(0,ROW * 7 - 1)
                //            MyApp.preferences.setString("Test", "TTT")
                //            Toast.makeText(holder.layout.context, "${dayList[position]}", Toast.LENGTH_SHORT).show()
            }
        }

//        holder.layout.itemDayText.text = dayList[position].date.toString()
        val tv = holder.layout.findViewById<TextView>(R.id.itemDayText)
        tv.text =  dayList[position].date.toString()

        tv.setTextColor(when(position % 7) {
            0 -> Color.RED
            6 -> Color.BLUE
            else -> Color.BLACK
        })


        if (tempMonth != dayList[position].month) {
            //            Log.d("aaa", "$position : {dayList[position].month.toString()}")
            tv.alpha = 0.2f
        }

    }

    override fun getItemCount(): Int {
        return ROW * 7
    }

//    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
//        super.onDetachedFromRecyclerView(recyclerView)
//    }
//
//    override fun onViewRecycled(holder: DayView) {
//        super.onViewRecycled(holder)
//    }


}