package com.example.customcalendar

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class AdapterPlan(val titles: MutableList<String>, val contents : MutableList<String> ):  RecyclerView.Adapter<AdapterPlan.ListAdapter>(){
    private var prevItem: Int = -1

    class ListAdapter(val layout: View): RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter {
        return ListAdapter(LayoutInflater.from(parent.context).inflate(R.layout.list_item_plan, parent, false))
    }

    override fun onBindViewHolder(holder: ListAdapter, position: Int) {
        if((position % 2) == 0) {
            holder.layout.setBackgroundColor(Color.argb(255, 235, 235, 235))
        } else {
            holder.layout.setBackgroundColor(Color.argb(255, 255, 255, 255))
        }

        val btnTrash = holder.layout.findViewById<ImageView>(R.id.btnRemove)
        btnTrash.visibility = View.GONE

        val btnModify = holder.layout.findViewById<ImageView>(R.id.btnModify)
        btnModify.visibility = View.GONE

        // convert Epoch string to Date string
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val longDate = titles[position].toLong()
        var selectedDate = Date(longDate)

        val tv = holder.layout.findViewById<TextView>(R.id.planTitle)
        tv.text = simpleDateFormat.format(selectedDate)

        val tv2 = holder.layout.findViewById<TextView>(R.id.planContent)
        tv2.text = contents[position]

        holder.layout.setOnClickListener {
            // Toast.makeText(holder.layout.context, tv.text.toString(), Toast.LENGTH_SHORT).show()
            if(prevItem != position)
                notifyItemChanged(prevItem)

            if(btnTrash.visibility == View.GONE) {
                btnTrash.visibility = View.VISIBLE
                btnModify.visibility = View.VISIBLE
            } else {
                notifyDataSetChanged()
//                notifyItemChanged(position)
            }

            prevItem = position.toInt()
        }

        btnTrash.setOnClickListener {
            val date = longDate.toString() // textDate.text.toString() // 날짜를 TextView로부터 얻어옴
            val db = MyDb(it.context, null)

            if (db.deleteEvent(date)) {
                Toast.makeText(it.context, "이벤트가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(it.context, "이벤트 삭제 실패.", Toast.LENGTH_SHORT).show()
            }

            val mainActivity = Intent(it.context,MainActivity::class.java)
            it.context.startActivity(mainActivity)

        }

        btnModify.setOnClickListener {
            val eventActivity = Intent(it.context,EventActivity::class.java)
            eventActivity.putExtra("date", longDate)
            it.context.startActivity(eventActivity)
        }

    }

    override fun getItemCount(): Int {
        return contents.size
    }
}