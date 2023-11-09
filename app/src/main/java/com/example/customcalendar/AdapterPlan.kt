package com.example.customcalendar

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar
import java.util.Date

class AdapterPlan(val titles: MutableList<String>, val contents : MutableList<String> ):  RecyclerView.Adapter<AdapterPlan.ListAdapter>(){
    class ListAdapter(val layout: View): RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter {
        return ListAdapter(LayoutInflater.from(parent.context).inflate(R.layout.list_item_plan, parent, false))
    }

    override fun onBindViewHolder(holder: ListAdapter, position: Int) {
        if((position % 2) == 0) {
            holder.layout.setBackgroundColor(Color.argb(255, 235, 235, 235))
//            holder.layout.setBackgroundColor(Color.argb(255, 200, 255, 255))
        }
        val btn = holder.layout.findViewById<Button>(R.id.btnRemove)
        btn.visibility = View.GONE

        val tv = holder.layout.findViewById<TextView>(R.id.planTitle)
        tv.text = titles[position]

        val tv2 = holder.layout.findViewById<TextView>(R.id.planContent)
        tv2.text = contents[position]

        holder.layout.setOnClickListener {
            Toast.makeText(holder.layout.context, tv.text.toString(), Toast.LENGTH_SHORT).show()

            if(btn.visibility == View.GONE)
                btn.visibility = View.VISIBLE
            else
                btn.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return contents.size
    }
}