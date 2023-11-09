package com.example.customcalendar

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String):String{
        return preferences.getString(key,defValue).toString()
    }

    fun setString(key: String, defValue: String){
        preferences.edit().putString(key, defValue).apply()
    }

    fun getLong(key: String, defValue: Long) : Long {
        return preferences.getLong(key,defValue)
    }

    fun setLong(key: String, defValue: Long){
        preferences.edit().putLong(key, defValue).apply()
    }
}