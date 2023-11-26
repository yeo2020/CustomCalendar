package com.example.customcalendar

import android.app.AlertDialog
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

class AdapterPlan(val ids :MutableList<Int>, val titles: MutableList<String>, val contents : MutableList<String> ):  RecyclerView.Adapter<AdapterPlan.ListAdapter>(){
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
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd      a hh:mm")
        val longDate = titles[position].toLong()
        var selectedDate = Date(longDate)

        val tv = holder.layout.findViewById<TextView>(R.id.planTitle)
        tv.text = simpleDateFormat.format(selectedDate)

        val tv2 = holder.layout.findViewById<TextView>(R.id.planContent)

        var tmpContent:String = contents[position]
        tmpContent = tmpContent.replace("happy", "\uD83D\uDE04")
        tmpContent = tmpContent.replace("smoking", "\uD83D\uDEAC")
        tmpContent = tmpContent.replace("beer", "\uD83C\uDF7A")
        tmpContent = tmpContent.replace("wine", "\uD83C\uDF77")
        tmpContent = tmpContent.replace("cocktail", "\uD83C\uDF78")
        tmpContent = tmpContent.replace("coffee", "☕")
        tmpContent = tmpContent.replace("cake", "\uD83C\uDF70")
        tmpContent = tmpContent.replace("money", "\uD83D\uDCB0")
//
//        when (mdContent) {
//            "happy" -> mdContent = "\uD83D\uDE04" // 😄
//            "smoking" -> mdContent = "\uD83D\uDEAC" // 🚬 (담배 이모티콘)
//            "beer" -> mdContent = "\uD83C\uDF7A" // 🍺 (맥주 이모티콘)
//            "wine" -> mdContent = "\uD83C\uDF77" // 🍷 (와인 이모티콘)
//            "cocktail" -> mdContent = "\uD83C\uDF78" // 🍸 (칵테일 이모티콘)
//            "coffee" -> mdContent = "\u2615" // ☕ (커피 이모티콘)
//            "cake" -> mdContent = "\uD83C\uDF70" // 🍰 (케이크 이모티콘)
//            "money" -> mdContent = "\uD83D\uDCB0" // 💰 (돈 이모티콘)
//            else -> {
//                // 기본적으로 입력된 텍스트 그대로 사용
//            }
//        }

        tv2.text = tmpContent


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
            val builder = AlertDialog.Builder(it.context)
            builder.setTitle("${tv.text.toString()} 삭제")
            builder.setMessage("정말 삭제 하시겠습니까?")

            builder.setPositiveButton("삭제") { dialog, which ->
                val id = ids[position].toString()
                val db = MyDb(it.context, null)

                if (db.deleteEvent(id)) {
                    Toast.makeText(it.context, "이벤트가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(it.context, "이벤트 삭제 실패.", Toast.LENGTH_SHORT).show()
                }

                val mainActivity = Intent(it.context,MainActivity::class.java)
                it.context.startActivity(mainActivity)
            }

            builder.setNegativeButton("취소") { dialog, which ->
                Toast.makeText(it.context, android.R.string.no, Toast.LENGTH_SHORT).show()
            }

//            builder.setNeutralButton("Maybe") { dialog, which ->
//                Toast.makeText(it.context,"Maybe", Toast.LENGTH_SHORT).show()
//            }
            builder.show()
        }

        btnModify.setOnClickListener {
            val eventActivity = Intent(it.context,EventActivity::class.java)
            eventActivity.putExtra("dbid", ids[position])
            it.context.startActivity(eventActivity)
        }

    }

    override fun getItemCount(): Int {
        return contents.size
    }
}