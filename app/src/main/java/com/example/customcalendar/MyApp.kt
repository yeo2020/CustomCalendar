package com.example.customcalendar

import android.app.Application

class MyApp: Application() {
    companion object{
        lateinit var preferences: PreferenceUtil
    }

    override fun onCreate() {
        preferences = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}