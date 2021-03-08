package com.basicbear.spammiewhammie

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.basicbear.spammiewhammie.database.Report

import com.basicbear.spammiewhammie.ui.contact_info.ContactInfoFragment
import com.basicbear.spammiewhammie.ui.contact_info.PersonalInfo
import com.basicbear.spammiewhammie.ui.main.MainFragment
import com.basicbear.spammiewhammie.ui.gov.ReportFragment
import com.basicbear.spammiewhammie.ui.history.ReportHistoryFragment
import com.google.android.gms.ads.MobileAds


private val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), NavigationCallbacks
{

    private lateinit var mainFragment: MainFragment
    private lateinit var contactInfoFragment: ContactInfoFragment
    private lateinit var contactInfo: PersonalInfo
    private lateinit var reportFragment: ReportFragment
    private lateinit var reportHistoryFragment: ReportHistoryFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this)

        contactInfo = PersonalInfo()
        contactInfo.getContactInfo(this)
        mainFragment = MainFragment.newInstance(contactInfo)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            goto_main()
            //Log.d(TAG, "support frag man back stack count after initial onCreate: " + supportFragmentManager.backStackEntryCount)
        }

    }

    override fun onBackPressed() {

        if(mainFragment.isVisible()){
            super.onBackPressed()
        }
        else if(this::reportFragment.isInitialized && (reportFragment.isVisible && reportFragment.WebViewCanGoBack())){
                reportFragment.WebViewGoBack()
        }
        else if(this::contactInfoFragment.isInitialized && (contactInfoFragment.isVisible && contactInfo.isEmpty())){
            super.onBackPressed()
        }
        else {
            goto_main()
        }
    }

    override fun goto_registration() {
        goto_gov_site(getString(R.string.federal_registration_url_postfix),false,null)
    }

    override fun goto_report() {
        goto_gov_site(getString(R.string.federal_report_url_postfix),true,null)
    }

    override fun goto_report(report: Report) {
        goto_gov_site(getString(R.string.federal_report_url_postfix),true,report)
    }

    override fun goto_verify_registration() {
        goto_gov_site(getString(R.string.federal_verify_registration_url_postfix),false,null)
    }

    override fun goto_reportHistory(){
        if(!this::reportHistoryFragment.isInitialized ){
            reportHistoryFragment = ReportHistoryFragment.newInstance()
        }

        supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, reportHistoryFragment)
                .commit()
    }

    override fun goto_contactInfo() {
        if(!this::contactInfoFragment.isInitialized )
            contactInfoFragment = ContactInfoFragment.newInstance(contactInfo)

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, contactInfoFragment)
            .commit()
    }

    override fun goto_main() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, mainFragment)
            .commit()
    }

    private fun goto_gov_site(path_feature:String, open_window_button_visibility:Boolean,report: Report?){
        val builder = Uri.Builder()

        builder.scheme("https")
                .authority(getString(R.string.federal_report_url))
                //.appendPath("Apps") // REMOVE IN PRODUCTION
                .appendPath(path_feature)
                .fragment(getString(R.string.federal_url_step1))
        Log.d(TAG,"URL String" + builder.toString())
        val uri = builder.build()
        reportFragment = ReportFragment.newInstance(contactInfo,uri.toString(),open_window_button_visibility,report)

        supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, reportFragment)
                .commit()
    }

}