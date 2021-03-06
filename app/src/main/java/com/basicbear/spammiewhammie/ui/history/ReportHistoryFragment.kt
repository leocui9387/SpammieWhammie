package com.basicbear.spammiewhammie.ui.history

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basicbear.spammiewhammie.NavigationCallbacks
import com.basicbear.spammiewhammie.R
import com.basicbear.spammiewhammie.ReportRepository
import com.basicbear.spammiewhammie.database.Report
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.DateFormat

class ReportHistoryFragment:Fragment() {
    companion object{
        fun newInstance():ReportHistoryFragment {


            return ReportHistoryFragment()
        }

    }

    private lateinit var reportRecyclerView: RecyclerView
    private var adapter:ReportAdapter? = ReportAdapter(emptyList())
    private lateinit var adView: AdView

    private var callbacks: NavigationCallbacks? = null
    private val reportHistoryViewModel:ReportHistoryViewModel by lazy {
        ViewModelProviders.of(this).get(ReportHistoryViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as NavigationCallbacks?
    }

    private fun updateUI(reports:List<Report>){
        reportRecyclerView.adapter = ReportAdapter(reports)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.report_history_fragment,container,false)

        adView = view.findViewById(R.id.report_history_adView)
        adView.loadAd(AdRequest.Builder().build())

        reportRecyclerView = view.findViewById(R.id.report_history_fragment_recycler_view) as RecyclerView
        reportRecyclerView.layoutManager = LinearLayoutManager(context)
        reportRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reportHistoryViewModel.reportListLiveData.observe(
                viewLifecycleOwner,
                Observer { reports ->
                    reports?.let {
                        updateUI(reports)
                    }
                }
        )
    }

    private inner class ReportHolder(view: View):RecyclerView.ViewHolder(view),View.OnClickListener{
        init {
            itemView.setOnClickListener(this)
        }

        private lateinit var myNumberTextView : TextView
        private lateinit var companyPhoneNumberTextView : TextView
        private lateinit var dateTextView : TextView

        private lateinit var isMobileCallChip:Chip
        private lateinit var haveDoneBusinessChip:Chip
        private lateinit var askedToStopChip:Chip

        private lateinit var companyNameTextView : TextView
        private lateinit var subjectMatterTextView : TextView
        private lateinit var commentsTextView : TextView

        private lateinit var summaryFrame:FrameLayout
        private lateinit var detailFrame:FrameLayout

        private lateinit var deleteButton:Button
        private lateinit var reportButton:Button

        private lateinit var report: Report

        override fun onClick(v: View?) {

            if(detailFrame.isVisible){
                detailFrame.visibility = View.GONE
            }
            else{
                detailFrame.visibility = View.VISIBLE
            }

        }

        fun bind(report: Report,position: Int){
            this.report = report

            if(position % 2 > 0)
                itemView.setBackgroundResource(R.color.green_light)
            else
                itemView.setBackgroundResource(R.color.white)

            bind_Views()
            bind_Values(report)

        }

        private fun bind_Views(){
            dateTextView = itemView.findViewById(R.id.report_history_item_phone_call_date)
            companyPhoneNumberTextView = itemView.findViewById(R.id.report_history_item_companyPhoneNumber)
            myNumberTextView = itemView.findViewById(R.id.report_history_item_phoneNumber)

            isMobileCallChip = itemView.findViewById(R.id.report_history_item_isMobileCall)
            haveDoneBusinessChip = itemView.findViewById(R.id.report_history_item_haveDoneBusiness)
            askedToStopChip = itemView.findViewById(R.id.report_history_item_askedToStop)

            companyNameTextView = itemView.findViewById(R.id.report_history_detail_companyName)
            commentsTextView  = itemView.findViewById(R.id.report_history_detail_comments)
            subjectMatterTextView = itemView.findViewById(R.id.report_history_detail_subjectMatter)

            detailFrame = itemView.findViewById(R.id.report_history_item_phone_call_details)
            summaryFrame = itemView.findViewById(R.id.report_history_item_report_summary)


            reportButton = itemView.findViewById(R.id.report_history_detail_report_button)
            reportButton.setOnClickListener{
                callbacks?.goto_report(report)
            }

            deleteButton = itemView.findViewById(R.id.report_history_detail_delete_button)
            deleteButton.setOnClickListener{
                reportHistoryViewModel.delete(report)
            }

            detailFrame.visibility = View.GONE
        }

        private fun bind_Values(report:Report){

            dateTextView.setText(report.dateOfCall + " " + report.timeOfCall +":"+ report.minuteOfCall)
            companyPhoneNumberTextView.setText(report.companyPhoneNumber)
            myNumberTextView.setText(report.phoneNumber)

            if(report.isMobileCall.equals("Y")) checkChip(isMobileCallChip)
            if(report.haveDoneBusiness.equals("Y")) checkChip(haveDoneBusinessChip)
            if(report.askedToStop.equals("Y")) checkChip(askedToStopChip)

            companyNameTextView.setText(report.companyName)
            commentsTextView.setText(report.comments)
            subjectMatterTextView.setText(report.subjectMatter)

        }

        private fun checkChip(chip:Chip){
            chip.setChipBackgroundColorResource(R.color.green)
            chip.setTextColor(ContextCompat.getColor(context!!,R.color.yellow))
        }

    }

    private inner class ReportAdapter(var reports: List<Report>):RecyclerView.Adapter<ReportHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportHolder {
            val view = layoutInflater.inflate(viewType, parent,false)


            return ReportHolder(view)
        }

        override fun onBindViewHolder(holder: ReportHolder, position: Int) {
            holder.bind(reports[position],position)
        }

        override fun getItemCount(): Int = reports.count()

        override fun getItemViewType(position: Int): Int {
            return R.layout.report_history_item
        }

    }
}