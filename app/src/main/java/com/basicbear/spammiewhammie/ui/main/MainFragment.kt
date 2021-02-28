package com.basicbear.spammiewhammie.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.basicbear.spammiewhammie.NavigationCallbacks
import com.basicbear.spammiewhammie.R

import com.basicbear.spammiewhammie.ui.contact_info.PersonalInfo
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

private const val TAG ="MainFragment"

private const val PARAM_contactInfo = TAG+"_ContactInfo"

class MainFragment() : Fragment() {

    companion object{
        fun newInstance(contactInfo: PersonalInfo):MainFragment{
            return MainFragment().apply {
                arguments=Bundle().apply {
                    putParcelable(PARAM_contactInfo,contactInfo)
                }
            }
        }
    }

    private lateinit var contactInfo: PersonalInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactInfo = arguments?.getParcelable(PARAM_contactInfo)?: PersonalInfo()
    }

    private var callbacks: NavigationCallbacks? = null

    private lateinit var adView: AdView
    private lateinit var initialRegistrationButton: Button
    private lateinit var verifyRegistrationButton: Button
    private lateinit var contactInfoButton: Button
    private lateinit var reportButton:Button
    private lateinit var reportHistoryButton:Button

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        initialRegistrationButton = view.findViewById(R.id.main_button_initial_registration)
        initialRegistrationButton.setOnClickListener {
            callbacks?.goto_registration()
        }

        verifyRegistrationButton = view.findViewById(R.id.main_button_verify_registration)
        verifyRegistrationButton.setOnClickListener {
            callbacks?.goto_verify_registration()
        }

        contactInfoButton = view.findViewById(R.id.main_button_contact_info)
        contactInfoButton.setOnClickListener {
            callbacks?.goto_contactInfo()
        }

        reportButton = view.findViewById(R.id.main_button_report_call)
        reportButton.setOnClickListener {
            callbacks?.goto_report()
        }

        reportHistoryButton = view.findViewById(R.id.main_button_report_history)
        reportHistoryButton.setOnClickListener {
            callbacks?.goto_reportHistory()
        }



        MobileAds.initialize(context)
        adView = view.findViewById(R.id.main_adView)
        val adRequest:AdRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        return view
    }

    override fun onStart() {
        super.onStart()

        if(contactInfo.isEmpty()) {
            callbacks?.goto_contactInfo()
            Toast.makeText(context,getString(R.string.contact_info_form_requirement_toast),Toast.LENGTH_LONG).show()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as NavigationCallbacks?
    }


    override fun onDetach() {
        super.onDetach()
        callbacks= null
    }

}