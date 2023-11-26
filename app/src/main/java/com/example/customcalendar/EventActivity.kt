package com.example.customcalendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import com.example.customcalendar.MyDb.Companion.CONTENT_COL
import java.text.SimpleDateFormat
import java.util.Date

class EventActivity : AppCompatActivity() {

    private var dbid: Int = 0
    private var longDate: Long = 0
    private lateinit var textDate: TextView
    private lateinit var textEvent: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        // 뒤로가기 버튼 및 저장 버튼 초기화
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnSave = findViewById<ImageView>(R.id.btnSave)

        // 날짜 및 이벤트 입력 관련 뷰 초기화
        textDate = findViewById<TextView>(R.id.textDate)
        textEvent = findViewById<EditText>(R.id.editTextText)

        // 뒤로가기 버튼 클릭 시 현재 액티비티 종료
        btnBack.setOnClickListener {
            finish()
        }

        // Intent로부터 dbid가 전달되었을 경우 해당 이벤트 정보 불러오기
        if (intent.hasExtra("dbid")) {
            dbid = intent.getIntExtra("dbid", 0)

            // 데이터베이스에서 해당 dbid에 해당하는 이벤트 정보 가져오기
            val db = MyDb(this, null)
            val cursor = db.getEventWithId(dbid)

            cursor?.moveToFirst()
            if (cursor?.count ?: 0 > 0) {
                longDate = cursor?.getString(cursor.getColumnIndex(MyDb.DATE_COL))?.toLong() ?: 0
                textEvent.setText(cursor?.getString(cursor.getColumnIndex(MyDb.CONTENT_COL)))
            } else {
                longDate = 0
                textEvent.setText("Failed to load data!")
            }
        } else {
            // dbid가 전달되지 않았을 경우 MyApp의 preferences에서 선택된 날짜 가져오기
            dbid = 0
            longDate = MyApp.preferences.getLong("selected_date", -1)
        }

        // 날짜 포맷 지정
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

        // longDate가 0보다 크면 해당 날짜로, 그렇지 않으면 현재 날짜로 TextView에 표시
        if (longDate > 0) {
            textDate.text = simpleDateFormat.format(Date(longDate))
        } else {
            textDate.text = simpleDateFormat.format(Date())
        }

        // 저장 버튼 클릭 시 이벤트 저장 또는 수정
        btnSave.setOnClickListener {
            val db = MyDb(this, null)
            val date = longDate.toString()
            var content = textEvent.text.toString() // var로 변경

            // 특정 키워드에 따라 이모티콘으로 변경
            when (content) {
                "happy" -> content = "\uD83D\uDE04" // 😄
                "smoking" -> content = "\uD83D\uDEAC" // 🚬 (담배 이모티콘)
                "beer" -> content = "\uD83C\uDF7A" // 🍺 (맥주 이모티콘)
                "wine" -> content = "\uD83C\uDF77" // 🍷 (와인 이모티콘)
                "cocktail" -> content = "\uD83C\uDF78" // 🍸 (칵테일 이모티콘)
                "coffee" -> content = "\u2615" // ☕ (커피 이모티콘)
                "cake" -> content = "\uD83C\uDF70" // 🍰 (케이크 이모티콘)
                "money" -> content = "\uD83D\uDCB0" // 💰 (돈 이모티콘)
                else -> {
                    // 기본적으로 입력된 텍스트 그대로 사용
                }
            }

            // dbid가 0이면 새로운 이벤트 추가, 그렇지 않으면 이벤트 수정
            if (dbid == 0) {
                db.addEvent(date, content)
                Toast.makeText(this, "이벤트가 저장되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                db.updateEvent(dbid.toString(), date, content)
                Toast.makeText(this, "이벤트가 수정되었습니다.", Toast.LENGTH_SHORT).show()
            }

            // 메인 액티비티로 이동
            val calMode = MyApp.preferences.getLong("cal_mode", 0).toInt()
            if(calMode == 1) {
                val rowActivity = Intent(this, RowActivity::class.java)
                startActivity(rowActivity)
            } else {
                val mainActivity = Intent(this, MainActivity::class.java)
                startActivity(mainActivity)
            }
        }

        // 시간 선택을 위한 TimePicker 초기화
        val timePicker = findViewById<TimePicker>(R.id.timePicker)

        // longDate를 이용하여 TimePicker의 시간 설정
        var tmpDate = Date(longDate)
        timePicker.minute = tmpDate.minutes
        timePicker.hour = tmpDate.hours

        // TimePicker의 시간이 변경되면 longDate를 갱신
        timePicker.setOnTimeChangedListener { _, i, i2 ->
            tmpDate = Date(longDate)
            tmpDate.hours = i
            tmpDate.minutes = i2

            longDate = tmpDate.time
        }
    }
}
