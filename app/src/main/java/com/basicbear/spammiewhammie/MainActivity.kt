package com.basicbear.spammiewhammie

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.basicbear.spammiewhammie.ui.main.MainFragment
import com.basicbear.spammiewhammie.ui.main.PhoneCall
import com.basicbear.spammiewhammie.ui.report.ReportFragment

private val TAG = "MainActivity"

class MainActivity : AppCompatActivity(),
    MainFragment.Callbacks{

    private lateinit var reportFragment: ReportFragment
    private lateinit var mainFragment: MainFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reportFragment = ReportFragment.newInstance()
        mainFragment = MainFragment.newInstance()
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.main_fragment_container, mainFragment, MainFragment.fragmentTag)
                    .commitNow()
            Log.d(TAG, "support frag man back stack count after initial onCreate: " + supportFragmentManager.backStackEntryCount)
        }

    }

    override fun onBackPressed() {

        if(mainFragment.isVisible())  {
            super.onBackPressed()
        }
        else if (reportFragment.isVisible()) {
            if (reportFragment.WebViewCanGoBack()) {
                reportFragment.WebViewGoBack()
                return
            }
            reportFragment.ClearWebView()
            supportFragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, mainFragment)
                    .commit()
        }
    }


    override fun onCallSelected(phoneCall: PhoneCall) {
        reportFragment.updatePhoneCall(phoneCall)

        supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, reportFragment, ReportFragment.fragmentTag)
                .commit()
    }


}