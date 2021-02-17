package com.basicbear.spammiewhammie

import android.app.Application

class SpammieWhammieApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        ReportRepository.initialize(this)

    }
}