package com.basicbear.spammiewhammie

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.basicbear.spammiewhammie.Validation.AreaCodes
import com.basicbear.spammiewhammie.Validation.DoNotCallApi
import com.basicbear.spammiewhammie.ui.contact_info.ContactInfoFragment
import com.basicbear.spammiewhammie.ui.contact_info.PersonalInfo
import com.basicbear.spammiewhammie.ui.main.MainFragment
import com.basicbear.spammiewhammie.ui.main.PhoneCall
import com.basicbear.spammiewhammie.ui.report.ReportFragment
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private val TAG = "MainActivity"

class MainActivity : AppCompatActivity(),
        MainFragment.Callbacks,
        ContactInfoFragment.Callbacks,
        ReportFragment.Callbacks
{

    private lateinit var dncApi: DoNotCallApi
    private lateinit var areaCodes: AreaCodes


    private lateinit var mainFragment: MainFragment
    private lateinit var contactInfoFragment: ContactInfoFragment
    private lateinit var contactInfo: PersonalInfo
    private lateinit var reportFragment: ReportFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contactInfo = PersonalInfo()
        contactInfo.getContactInfo(this)

        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.federal_report_url))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        dncApi = retrofit.create(DoNotCallApi::class.java)
        areaCodes = AreaCodes(dncApi)


        mainFragment = MainFragment(contactInfo)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.main_fragment_container, mainFragment)
                    .commitNow()
            //Log.d(TAG, "support frag man back stack count after initial onCreate: " + supportFragmentManager.backStackEntryCount)
        }


    }

    override fun onBackPressed() {

        if(mainFragment.isVisible())  {
            super.onBackPressed()
        }
        else if(reportFragment.isVisible){
            if(reportFragment.WebViewCanGoBack()){
                reportFragment.WebViewGoBack()
            }else {
                super.onBackPressed()
            }
        }
        else {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, mainFragment)
                    .commit()
        }
    }

    override fun onCallSelected(phoneCall: PhoneCall) {
        reportFragment = ReportFragment(contactInfo,phoneCall)

        supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, reportFragment)
                .commit()

    }

    override fun onMenuContactInfoSelected() {
        contactInfoFragment = ContactInfoFragment(contactInfo)

        supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, contactInfoFragment)
                .commit()
    }

    override fun onContactInfoSaveSelected() {
        contactInfo.GetThisPhoneNumber(this)
        contactInfo.saveToFile(this)

        supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, mainFragment)
                .commit()
    }


    override fun onComplaintSubmission() {



        supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, mainFragment)
                .commit()
    }
}