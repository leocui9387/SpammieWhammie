package com.basicbear.spammiewhammie

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.basicbear.spammiewhammie.ui.contact_info.ContactInfoFragment
import com.basicbear.spammiewhammie.ui.contact_info.PersonalInfo
import com.basicbear.spammiewhammie.ui.main.MainFragment
import com.basicbear.spammiewhammie.ui.main.PhoneCall
import com.basicbear.spammiewhammie.ui.report.ReportFragment

private val TAG = "MainActivity"

class MainActivity : AppCompatActivity(),
        MainFragment.Callbacks,
        ContactInfoFragment.Callbacks,
        ReportFragment.Callbacks
{

    private lateinit var reportFragment: ReportFragment
    private lateinit var mainFragment: MainFragment
    private lateinit var contactInfoFragment: ContactInfoFragment
    private lateinit var contactInfo: PersonalInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contactInfo= PersonalInfo()
        contactInfo.getContactInfo(this)

        reportFragment = ReportFragment.newInstance(contactInfo)
        mainFragment = MainFragment.newInstance(contactInfo)

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
        }else {
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

    override fun onMenuContactInfoSelected() {
        contactInfoFragment = ContactInfoFragment.newInstance(contactInfo)

        supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, contactInfoFragment, ContactInfoFragment.fragmentTag)
                .commit()
    }

    override fun onContactInfoSaveSelected() {
        contactInfo.saveToFile(this)
    }

    override fun closeSoftKeyboard(view: View) {
        if(view != null){
            val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken,0)
        }
    }
}