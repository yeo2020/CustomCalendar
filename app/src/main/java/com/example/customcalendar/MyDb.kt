package com.example.customcalendar

import android.app.usage.UsageEvents
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// SQLiteOpenHelper를 상속하여 데이터베이스 관련 기능을 제공하는 MyDb 클래스 정의
class MyDb(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // 데이터베이스 생성 시 호출되는 메서드
    override fun onCreate(db: SQLiteDatabase) {
        // 데이터베이스에 테이블 생성하는 쿼리
        val query = ("CREATE TABLE " + TABLE_NAME + " (" +
                ID_COL + " INTEGER PRIMARY KEY, " +
                DATE_COL + " TEXT," +
                CONTENT_COL + " TEXT" + ")")

        // 쿼리 실행
        db.execSQL(query)
    }

    // 데이터베이스 업그레이드 시 호출되는 메서드
    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // 기존 테이블이 존재하면 삭제하고 다시 생성
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    // 데이터베이스에 이벤트 추가하는 메서드
    fun addEvent(date: String, content: String) {
        // ContentValues 객체 생성
        val values = ContentValues()

        // 키-값 쌍으로 값을 설정
        values.put(DATE_COL, date)
        values.put(CONTENT_COL, content)

        // 쓰기 모드의 데이터베이스 열기
        val db = this.writableDatabase

        // 데이터베이스에 값 삽입
        db.insert(TABLE_NAME, null, values)

        // 데이터베이스 닫기
        db.close()
    }

    // 데이터베이스에서 모든 이벤트 정보를 가져오는 메서드
    fun getEvent(): Cursor? {
        val db = this.readableDatabase

        // 이벤트 날짜를 오름차순으로 정렬하여 데이터를 가져옴
        val query = "SELECT * FROM $TABLE_NAME ORDER BY $DATE_COL ASC"
        return db.rawQuery(query, null)
    }

    // 특정 ID에 해당하는 이벤트 정보를 가져오는 메서드
    fun getEventWithId(id: Int): Cursor? {
        val db = this.readableDatabase

        val args = arrayOf(id.toString())
        // 이벤트 날짜를 오름차순으로 정렬하여 데이터를 가져옴
        val query = "SELECT * FROM $TABLE_NAME WHERE $ID_COL = ? LIMIT 1"
        return db.rawQuery(query, args)
    }

    // 특정 ID에 해당하는 이벤트를 삭제하는 메서드
    fun deleteEvent(id: String): Boolean {
        val db = this.writableDatabase
        val whereClause = "$ID_COL = ?"
        val whereArgs = arrayOf(id)

        // 해당 ID의 이벤트 삭제
        val deletedRows = db.delete(TABLE_NAME, whereClause, whereArgs)

        // 데이터베이스 닫기
        db.close()

        // 삭제된 행이 1개 이상이면 성공적으로 삭제된 것으로 간주
        return deletedRows > 0
    }

    // 특정 ID에 해당하는 이벤트를 업데이트하는 메서드
    fun updateEvent(id: String, date: String, content: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()

        // 업데이트할 값 설정
        values.put(DATE_COL, date)
        values.put(CONTENT_COL, content)

        // 해당 ID의 이벤트 업데이트
        val rowsAffected = db.update(TABLE_NAME, values, "$ID_COL = ?", arrayOf(id))

        // 데이터베이스 닫기
        db.close()

        // 업데이트된 행이 1개 이상이면 성공적으로 업데이트된 것으로 간주
        return rowsAffected > 0
    }

    // 동반 객체로 정적 상수 및 메서드 정의
    companion object {
        private val DATABASE_NAME = "TEAM_CALENDAR_GWNU"  // 데이터베이스 이름
        private val DATABASE_VERSION = 1  // 데이터베이스 버전
        val TABLE_NAME = "tcg_table"  // 테이블 이름
        val ID_COL = "id"  // ID 컬럼 이름
        val DATE_COL = "event_date"  // 날짜 컬럼 이름
        val CONTENT_COL = "event_content"  // 내용 컬럼 이름
    }
}
