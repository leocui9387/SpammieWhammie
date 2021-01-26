package com.basicbear.spammiewhammie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.basicbear.spammiewhammie.ui.contact_info.ContactInfoFragment
import com.basicbear.spammiewhammie.ui.main.MainFragment

class ContactInfoActivity  : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_contact_info)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.contact_info_fragment_container, ContactInfoFragment.newInstance())
                    .commitNow()
        }

    }


}