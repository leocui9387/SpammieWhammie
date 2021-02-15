package com.basicbear.spammiewhammie.ui.contact_info

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.basicbear.spammiewhammie.NavigationCallbacks
import com.basicbear.spammiewhammie.R
import com.google.android.material.textfield.TextInputEditText

private const val TAG="ContactInfoFragment"
private const val PARAM_contactInfo = TAG +"_ContactInfo"

class ContactInfoFragment():Fragment() {

    companion object{

        fun newInstance(contactInfo: PersonalInfo): ContactInfoFragment {
            return ContactInfoFragment().apply {
                    arguments=Bundle().apply {
                    putParcelable(PARAM_contactInfo,contactInfo)
                }
            }
        }

    }

    private lateinit var personalInfo: PersonalInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        personalInfo = arguments?.getParcelable(PARAM_contactInfo)?: PersonalInfo()
    }

    private lateinit var myPhoneNumber: TextInputEditText
    private lateinit var myPhoneNumberOverride: Switch
    private lateinit var myEmail: TextInputEditText
    private lateinit var firstName: TextInputEditText
    private lateinit var lastName: TextInputEditText
    private lateinit var streetAddress1: TextInputEditText
    private lateinit var streetAddress2: TextInputEditText
    private lateinit var cityInfo: TextInputEditText
    private lateinit var stateInfo: TextInputEditText
    private lateinit var zipInfo: TextInputEditText
    private lateinit var saveButton: Button

    private var callbacks: NavigationCallbacks? = null
    private val filesDir = context?.applicationContext?.filesDir

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as NavigationCallbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.contact_info_fragment, container, false)

        myPhoneNumber = view.findViewById(R.id.contact_info_form_phone_number_override)
        myPhoneNumber.setText(personalInfo.MyPhoneNumber)

        myPhoneNumberOverride = view.findViewById(R.id.contact_info_form_switch_phone_number_override)
        myPhoneNumberOverride.isChecked = personalInfo.MyPhoneNumberOverride

        myEmail = view.findViewById(R.id.contact_info_form_email)
        myEmail.setText(personalInfo.MyEmail)

        firstName = view.findViewById(R.id.contact_info_form_first_name)
        firstName.setText(personalInfo.FirstName)

        lastName = view.findViewById(R.id.contact_info_form_last_name)
        lastName.setText(personalInfo.LastName)

        streetAddress1= view.findViewById(R.id.contact_info_form_street_address_1)
        streetAddress1.setText(personalInfo.StreetAddress)

        streetAddress2= view.findViewById(R.id.contact_info_form_street_address_2)
        streetAddress2.setText(personalInfo.StreetAddress2)

        cityInfo = view.findViewById(R.id.contact_info_form_city)
        cityInfo.setText(personalInfo.City)

        stateInfo = view.findViewById(R.id.contact_info_form_state)
        stateInfo.setText(personalInfo.State)

        zipInfo = view.findViewById(R.id.contact_info_form_zip_code)
        zipInfo.setText(personalInfo.ZIP)

        saveButton = view.findViewById(R.id.contact_info_save_button)
        saveButton.setOnClickListener {

            personalInfo.MyPhoneNumberOverride = myPhoneNumberOverride.isChecked
            if(personalInfo.MyPhoneNumberOverride){
                personalInfo.MyPhoneNumber = myPhoneNumber.text.toString()
            }else{
                personalInfo.GetThisPhoneNumber(requireActivity())
            }
            personalInfo.MyEmail = myEmail.text.toString()
            personalInfo.FirstName = firstName.text.toString()
            personalInfo.LastName = lastName.text.toString()
            personalInfo.StreetAddress = streetAddress1.text.toString()
            personalInfo.StreetAddress2 = streetAddress2.text.toString()
            personalInfo.City = cityInfo.text.toString()
            personalInfo.State = stateInfo.text.toString()
            personalInfo.ZIP = zipInfo.text.toString()

            val entryValidation = personalInfo.validate()

            if(!entryValidation.isEmpty() ) {
                personalInfo.saveToFile(requireActivity())
                callbacks?.goto_main()
            }else{
                Toast.makeText(requireContext(),entryValidation, Toast.LENGTH_LONG)
            }

        }


        return view
    }


}