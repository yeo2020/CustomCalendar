package com.example.customcalendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.customcalendar.MyDb.Companion.CONTENT_COL
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.Date

class EventActivity(): AppCompatActivity() {
    private var longDate: Long = 0
    private lateinit var textDate: TextView // 이벤트 날짜를 나타내는 TextView
    private lateinit var textEvent: EditText // 이벤트 내용을 입력하는 EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event) // activity_event 레이아웃을 화면에 표시

        val btnClose = findViewById<Button>(R.id.btnBack) // "닫기" 버튼
        val btnSave = findViewById<Button>(R.id.btnSave) // "저장" 버튼
        val btnDel = findViewById<Button>(R.id.btnDel) // "삭제" 버튼
        val btnUpdate = findViewById<Button>(R.id.btnUpdate) //"수정" 버튼

        textDate = findViewById<TextView>(R.id.textDate) // 날짜를 표시하는 TextView
        textEvent = findViewById<EditText>(R.id.editTextText) // 이벤트 내용을 입력하는 EditText

        // "닫기" 버튼 클릭 시 MainActivity로 돌아가는 이벤트 처리
        btnClose.setOnClickListener {
            val mainActivity = Intent(this, MainActivity::class.java)
            startActivity(mainActivity)
        }

        if (intent.hasExtra("date")) {
            // 'date'라는 키로 데이터가 인텐트로 전달되었는지 확인합니다.
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            // 'simpleDateFormat'는 날짜를 지정된 형식("yyyy-MM-dd")으로 문자열로 포맷팅하기 위한 SimpleDateFormat 객체를 생성합니다.
            longDate = intent.getLongExtra("date", -1)
            var selectedDate = Date(intent.getLongExtra("date", -1))
            // 'intent.getLongExtra("date", -1)'을 사용하여 'date' 키로 전달된 데이터를 읽어와서 'selectedDate' 변수에 저장합니다.
            // 'getLongExtra' 메서드는 'date' 키에 해당하는 값이 없을 경우 기본값으로 -1을 사용합니다.
            textDate.text = simpleDateFormat.format(selectedDate)
            // 'selectedDate'를 'simpleDateFormat'을 사용하여 "yyyy-MM-dd" 형식의 문자열로 변환하고,
            // 이 문자열을 'textDate'의 'text' 속성에 설정하여 화면에 표시합니다.
        } else {
            // 'date' 키로 데이터가 전달되지 않았을 경우 아래 코드가 실행됩니다.
            var selectedDate = Date()  // 'selectedDate' 변수에 현재 날짜를 저장합니다.
        }

        btnSave.setOnClickListener {
            val date = longDate.toString() // textDate.text.toString() // 날짜를 TextView로부터 얻어옴
            val content = textEvent.text.toString() // 이벤트 내용을 EditText로부터 얻어옴
            val db = MyDb(this, null)  // 이벤트 정보를 데이터베이스에 추가
            db.addEvent(date, content)  // 사용자에게 메시지 표시
            Toast.makeText(this, "이벤트가 저장되었습니다.", Toast.LENGTH_SHORT).show()

            val mainActivity = Intent(this, MainActivity::class.java)
            startActivity(mainActivity)
        }

        btnDel.setOnClickListener {
            val date = longDate.toString() // textDate.text.toString() // 날짜를 TextView로부터 얻어옴
            val db = MyDb(this, null)

            // 이벤트 정보를 데이터베이스에서 삭제
            if (db.deleteEvent(date)) {
                Toast.makeText(this, "이벤트가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "이벤트 삭제 실패.", Toast.LENGTH_SHORT).show()
            }

            val mainActivity = Intent(this, MainActivity::class.java)
            startActivity(mainActivity)

        }




    }
}