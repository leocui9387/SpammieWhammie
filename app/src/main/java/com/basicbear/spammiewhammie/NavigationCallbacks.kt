package com.basicbear.spammiewhammie

import com.basicbear.spammiewhammie.database.Report

interface NavigationCallbacks {
    fun goto_registration()
    fun goto_report()
    fun goto_report(report: Report)
    fun goto_contactInfo()
    fun goto_main()
    fun goto_verify_registration()
    fun goto_reportHistory()
}