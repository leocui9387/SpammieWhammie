package com.basicbear.spammiewhammie.ui.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.CallLog
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basicbear.spammiewhammie.R

import com.basicbear.spammiewhammie.ui.contact_info.PersonalInfo
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.DateFormat


private const val TAG ="MainFragment"
private const val PERMISSIONS_REQUEST_READ_CALL_LOG = 100

private const val contactInfoTag = "ContactInfoParameter"

class MainFragment : Fragment() {

    companion object {
        val fragmentTag = "MainFragment"
        fun newInstance(contactInfo: PersonalInfo): MainFragment {
            return MainFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(contactInfoTag, contactInfo)
                }
            }
        }
    }

    interface Callbacks{
        fun onCallSelected(phoneCall: PhoneCall)
        fun onMenuContactInfoSelected()
    }

    private var callbacks:Callbacks? = null

    private var phoneCallAdap: PhoneCallAdapter = PhoneCallAdapter(emptyList())

    private lateinit var adView: AdView
    private lateinit var phoneCallRV: RecyclerView
    private lateinit var phoneCallLogs:List<PhoneCall>
    private lateinit var contactInfo:PersonalInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        contactInfo = arguments?.getParcelable(contactInfoTag)?:PersonalInfo()

        val view = inflater.inflate(R.layout.main_fragment, container, false)
        phoneCallRV = view.findViewById(R.id.main_fragment_recycler_view)
        phoneCallRV.layoutManager = LinearLayoutManager(requireContext())
        phoneCallLogs  = getCalls(requireContext())
        phoneCallAdap = PhoneCallAdapter(phoneCallLogs)
        phoneCallRV.adapter = phoneCallAdap

        MobileAds.initialize(context)
        adView = view.findViewById(R.id.main_adView)
        val adRequest:AdRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callbacks = context as Callbacks?

        Log.d(TAG, "attached")
        if(context.checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    arrayOf(Manifest.permission.READ_CALL_LOG),
                    PERMISSIONS_REQUEST_READ_CALL_LOG
            )
        }



    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_settings, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.main_menu_item_contact_info) {
            callbacks?.onMenuContactInfoSelected()
            return true
        }
        return false
    }

    private fun getCalls(context: Context): List<PhoneCall> {

        val managedCursor: Cursor = context.contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                null,
                null,
                null,
                null
        )!!

        val number: Int = managedCursor.getColumnIndex(CallLog.Calls.NUMBER)
        val via_number: Int = managedCursor.getColumnIndex(CallLog.Calls.VIA_NUMBER)
        val type: Int = managedCursor.getColumnIndex(CallLog.Calls.TYPE)
        val date: Int = managedCursor.getColumnIndex(CallLog.Calls.DATE)
        val duration: Int = managedCursor.getColumnIndex(CallLog.Calls.DURATION)

        Log.d(TAG, "got cursor")

        var calls:MutableList<PhoneCall> = mutableListOf<PhoneCall>()
        var callBuff: PhoneCall
        while (managedCursor.moveToNext()) {
            callBuff = PhoneCall()

            callBuff.number =  managedCursor.getString(number)
            callBuff.via_number =  managedCursor.getString(via_number)
            callBuff.type = managedCursor.getInt(type)
            callBuff.date = managedCursor.getLong(date)
            callBuff.duration = managedCursor.getLong(duration)

            calls.add(callBuff)
        }
        managedCursor.close()

        return calls
    }

    private inner class PhoneCallAdapter(var phoneCallLog: List<PhoneCall>):RecyclerView.Adapter<PhoneCallHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneCallHolder {
            val view = layoutInflater.inflate(viewType, parent, false)
            return PhoneCallHolder(view)
        }

        override fun onBindViewHolder(holder: PhoneCallHolder, position: Int) {
            holder.bind(phoneCallLog[position])
        }

        override fun getItemCount(): Int = phoneCallLog.size

        override fun getItemViewType(position: Int): Int {
            return R.layout.list_item_phone_call
        }

    }

    private inner class PhoneCallHolder(view: View):RecyclerView.ViewHolder(view),View.OnClickListener{
        init {
            itemView.setOnClickListener(this)
        }
        private lateinit var numberTextView : TextView
        private lateinit var dateTextView : TextView
        private lateinit var durationTextView : TextView
        private lateinit var typeTextView : TextView
        private lateinit var detailFrame : FrameLayout
        private lateinit var summaryFrame : FrameLayout
        private lateinit var reportButton: FloatingActionButton

        private lateinit var phoneCall:PhoneCall

        override fun onClick(v: View?) {

            if(detailFrame.isVisible) detailFrame.visibility = View.GONE
            else detailFrame.visibility = View.VISIBLE

        }

        fun bind(call: PhoneCall){
            phoneCall = call

            bind_Views()
            bind_Values()

        }

        private fun bind_Views(){
            dateTextView = itemView.findViewById(R.id.list_item_phone_call_date)
            numberTextView = itemView.findViewById(R.id.list_item_phone_call_number)
            durationTextView = itemView.findViewById(R.id.list_item_phone_call_duration)
            typeTextView = itemView.findViewById(R.id.list_item_phone_call_type)

            detailFrame = itemView.findViewById(R.id.list_item_phone_call_details)
            summaryFrame = itemView.findViewById(R.id.list_item_phone_call_summary)

            reportButton = itemView.findViewById(R.id.list_item_phone_call_report_button)
            reportButton.setOnClickListener{
                Log.d(TAG,"phone call data: " + phoneCall)
                callbacks?.onCallSelected(phoneCall)
            }

            detailFrame.visibility = View.GONE
        }

        private fun bind_Values(){
            dateTextView.setText(
                    DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(
                            phoneCall.date
                    )
            )
            numberTextView.setText(phoneCall.FormatedNumber())
            durationTextView.setText(phoneCall.duration.toString())
            typeTextView.setText(phoneCall.TypeString())
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDetach() {
        super.onDetach()
        callbacks= null
    }



}