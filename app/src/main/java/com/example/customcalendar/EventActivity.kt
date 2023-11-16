package com.example.customcalendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import com.example.customcalendar.MyDb.Companion.CONTENT_COL
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.Date

class EventActivity(): AppCompatActivity() {
    private var dbid: Int = 0
    private var longDate: Long = 0
    private lateinit var textDate: TextView // 이벤트 날짜를 나타내는 TextView
    private lateinit var textEvent: EditText // 이벤트 내용을 입력하는 EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event) // activity_event 레이아웃을 화면에 표시

        val btnClose = findViewById<Button>(R.id.btnBack) // "닫기" 버튼
        val btnSave = findViewById<Button>(R.id.btnSave) // "저장" 버튼

        textDate = findViewById<TextView>(R.id.textDate) // 날짜를 표시하는 TextView
        textEvent = findViewById<EditText>(R.id.editTextText) // 이벤트 내용을 입력하는 EditText

        // "닫기" 버튼 클릭 시 MainActivity로 돌아가는 이벤트 처리
        btnClose.setOnClickListener {
            val mainActivity = Intent(this, MainActivity::class.java)
            startActivity(mainActivity)
        }

        if (intent.hasExtra("dbid")) {
            // modify data
            dbid = intent.getIntExtra("dbid", 0)

            val db = MyDb(this, null)
            val cursor = db.getEventWithId(dbid)

            cursor!!.moveToFirst()
            if(cursor!!.count > 0) {
                longDate = cursor.getString(cursor.getColumnIndex(MyDb.DATE_COL)).toLong()
                textEvent.setText(cursor.getString(cursor.getColumnIndex(MyDb.CONTENT_COL)))
            } else {
                longDate = 0
                textEvent.setText("Failed to load data!")
            }
        } else {
            // new data to be added
            dbid = 0
            longDate = MyApp.preferences.getLong("selected_date",-1)

        }

        // 'simpleDateFormat'는 날짜를 지정된 형식("yyyy-MM-dd")으로 문자열로 포맷팅하기 위한 SimpleDateFormat 객체를 생성합니다.
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

        if(longDate > 0) {
            textDate.text = simpleDateFormat.format(Date(longDate))
        } else {
            textDate.text = simpleDateFormat.format(Date())
        }

        btnSave.setOnClickListener {
            val db = MyDb(this, null)  // 이벤트 정보를 데이터베이스에 추가
            val date = longDate.toString() // textDate.text.toString() // 날짜를 TextView로부터 얻어옴
            val content = textEvent.text.toString() // 이벤트 내용을 EditText로부터 얻어옴

            if(dbid == 0)  {
                // new data
                db.addEvent(date, content)  // 사용자에게 메시지 표시
                Toast.makeText(this, "이벤트가 저장되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                // modify data
                db.updateEvent(dbid.toString(), date, content)
                Toast.makeText(this, "이벤트가 수정되었습니다.", Toast.LENGTH_SHORT).show()
            }

            val mainActivity = Intent(this, MainActivity::class.java)
            startActivity(mainActivity)
        }


        /************************************************************
         * TimePicker
         * ***********************************************************/
        val timePicker = findViewById<TimePicker>(R.id.timePicker)

        val tmpDate = Date(longDate)
        timePicker.minute = tmpDate.minutes
        timePicker.hour = tmpDate.hours

        timePicker.setOnTimeChangedListener { timePicker, i, i2 ->
            // parameter 'i' means 24 hours, ex) PM2 = 14, AM2 = 2
            // parameter 'i2' means minutes
            val tmpDate = Date(longDate)
            tmpDate.hours = i
            tmpDate.minutes = i2

            longDate = tmpDate.time
        }
    }
}