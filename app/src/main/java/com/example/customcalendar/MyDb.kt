package com.example.customcalendar

import android.app.usage.UsageEvents
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDb(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // below is a sqlite query, where column names
        // along with their data types is given
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                DATE_COL + " TEXT," +
                CONTENT_COL + " TEXT" + ")")

        // we are calling sqlite
        // method for executing our query
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    // This method is for adding data in our database
    fun addEvent(date : String, content : String ){

        // below we are creating
        // a content values variable
        val values = ContentValues()

        // we are inserting our values
        // in the form of key-value pair
        values.put(DATE_COL, date)
        values.put(CONTENT_COL, content)

        // here we are creating a
        // writable variable of
        // our database as we want to
        // insert value in our database
        val db = this.writableDatabase

        // all values are inserted into database
        db.insert(TABLE_NAME, null, values)

        // at last we are
        // closing our database
        db.close()
    }

    // below method is to get
    // all data from our database
    fun getEvent(): Cursor? {
        val db = this.readableDatabase

        // 이벤트 날짜를 오름차순으로 정렬하여 데이터를 가져옵니다.
        val query = "SELECT * FROM $TABLE_NAME ORDER BY $DATE_COL ASC"
        return db.rawQuery(query, null)
    }

    fun getEventWithId(id:Int): Cursor? {
        val db = this.readableDatabase

        val args = arrayOf(id.toString())
        // 이벤트 날짜를 오름차순으로 정렬하여 데이터를 가져옵니다.
        val query = "SELECT * FROM $TABLE_NAME WHERE $ID_COL = ? LIMIT 1"
        return db.rawQuery(query, args)
    }


    fun deleteEvent(id: String): Boolean {
        val db = this.writableDatabase
        val whereClause = "$ID_COL = ?"
        val whereArgs = arrayOf(id)

        // Delete the event that matches the date
        val deletedRows = db.delete(TABLE_NAME, whereClause, whereArgs)

        db.close()

        return deletedRows > 0
    }

    fun updateEvent(id: String, date: String, content: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(DATE_COL, date)
        values.put(CONTENT_COL, content)

        // 해당 날짜의 이벤트를 업데이트
        val rowsAffected = db.update(TABLE_NAME, values, "$ID_COL = ?", arrayOf(id))

        db.close()

        // rowsAffected 값이 1 이상이면 업데이트가 성공한 것으로 간주
        return rowsAffected > 0

    }

    companion object{
        // here we have defined variables for our database

        // below is variable for database name
        private val DATABASE_NAME = "TEAM_CALENDAR_GWNU"

        // below is the variable for database version
        private val DATABASE_VERSION = 1

        // below is the variable for table name
        val TABLE_NAME = "tcg_table"

        // below is the variable for id column
        val ID_COL = "id"

        // below is the variable for name column
        val DATE_COL = "event_date"

        // below is the variable for age column
        val CONTENT_COL = "event_content"
    }
}