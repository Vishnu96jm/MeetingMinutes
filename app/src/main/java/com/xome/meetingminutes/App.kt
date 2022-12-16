package com.xome.meetingminutes

import android.app.Application
import java.text.SimpleDateFormat
import java.util.*

class App : Application() {

    companion object {
        lateinit var instance: App

        private val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        fun getDate(): Date? {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            return sdf.parse("$year-$month-$day")
        }


    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }


}