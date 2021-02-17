package com.basicbear.spammiewhammie

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telecom.TelecomManager
import android.view.accessibility.AccessibilityEvent
import androidx.appcompat.app.AppCompatActivity

import com.basicbear.spammiewhammie.ui.contact_info.ContactInfoFragment
import com.basicbear.spammiewhammie.ui.contact_info.PersonalInfo
import com.basicbear.spammiewhammie.ui.main.MainFragment
import com.basicbear.spammiewhammie.ui.gov.ReportFragment
import com.basicbear.spammiewhammie.ui.history.ReportHistoryDetailFragment
import com.basicbear.spammiewhammie.ui.history.ReportHistoryFragment


private val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), NavigationCallbacks
{

    private lateinit var mainFragment: MainFragment
    private lateinit var contactInfoFragment: ContactInfoFragment
    private lateinit var contactInfo: PersonalInfo
    private lateinit var reportFragment: ReportFragment
    private lateinit var reportHistoryFragment: ReportHistoryFragment
    private lateinit var reportHistoryDetailFragment: ReportHistoryDetailFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        if(mainFragment.isVisible())  {
            super.onBackPressed()
        }
        else if(
                this::reportFragment.isInitialized &&
                (reportFragment.isVisible && reportFragment.WebViewCanGoBack())
        ){
                reportFragment.WebViewGoBack()
        }else if(
                this::reportHistoryDetailFragment.isInitialized &&
                reportHistoryDetailFragment.isVisible
        ){
            reportFragment.WebViewGoBack()
        } else {
            goto_main()
        }
    }

    override fun goto_registration() {
        goto_gov_site(getString(R.string.federal_registration_url_postfix),false)
    }

    override fun goto_report() {
        goto_gov_site(getString(R.string.federal_report_url_postfix),true)
    }

    override fun goto_verify_registration() {
        goto_gov_site(getString(R.string.federal_verify_registration_url_postfix),false)
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

    private fun goto_gov_site(path_feature:String, open_window_button_visibility:Boolean){
        val builder = Uri.Builder()

        builder.scheme("https")
                .authority(getString(R.string.federal_report_url))
                .appendPath(path_feature)
                .fragment(getString(R.string.federal_url_step1))

        val uri = builder.build()
        reportFragment = ReportFragment.newInstance(contactInfo,uri.toString(),open_window_button_visibility)

        supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, reportFragment)
                .commit()
    }

}