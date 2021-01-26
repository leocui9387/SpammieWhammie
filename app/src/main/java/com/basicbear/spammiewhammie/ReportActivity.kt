package com.basicbear.spammiewhammie

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.basicbear.spammiewhammie.ui.contact_info.PersonalInfo
import com.basicbear.spammiewhammie.ui.main.PhoneCall
import com.basicbear.spammiewhammie.ui.report.ReportFragment

class ReportActivity:AppCompatActivity() {

    interface Callbacks {
        fun onBackPressed()
    }

    private var reportFrag:ReportFragment = ReportFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_report)

        if (savedInstanceState == null) {

            //val contactInfo:PersonalInfo = intent.getParcelableExtra(ReportFragment.PersonalInfo_ExtraTag)?: PersonalInfo()


            supportFragmentManager.beginTransaction()
                    .add(R.id.report_fragment_container,reportFrag )
                    .commitNow()
        }


    }

    override fun onBackPressed() {

        if(reportFrag.WebViewCanGoBack()){
            reportFrag.WebViewGoBack()
        }else {
            super.onBackPressed()
        }

    }

}