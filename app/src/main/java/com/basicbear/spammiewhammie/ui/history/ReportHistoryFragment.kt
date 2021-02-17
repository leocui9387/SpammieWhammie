package com.basicbear.spammiewhammie.ui.history

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basicbear.spammiewhammie.R
import com.basicbear.spammiewhammie.database.Report
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ReportHistoryFragment:Fragment() {
    companion object{
        fun newInstance():ReportHistoryFragment {

            return ReportHistoryFragment()
        }

    }
    
    
    
    private lateinit var reportRecyclerView: WebView
    private var adapter:ReportAdapter? = ReportAdapter()
    
    private inner class ReportHolder(view: View):RecyclerView.ViewHolder(view),View.OnClickListener{
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

        private lateinit var report: Report

        override fun onClick(v: View?) {

            if(detailFrame.isVisible) detailFrame.visibility = View.GONE
            else detailFrame.visibility = View.VISIBLE

        }

        fun bind(report: Report){
            this.report = report

            bind_Views()
            bind_Values()

        }

        private fun bind_Views(){
            dateTextView = itemView.findViewById(R.id.report_history_item_phone_call_date)
            numberTextView = itemView.findViewById(R.id.report_history_item_phone_call_number)
            durationTextView = itemView.findViewById(R.id.report_history_item_phone_call_duration)
            typeTextView = itemView.findViewById(R.id.report_history_item_phone_call_type)

            detailFrame = itemView.findViewById(R.id.report_history_item_phone_call_details)
            summaryFrame = itemView.findViewById(R.id.report_history_item_report_summary)

            reportButton = itemView.findViewById(R.id.report_history_item_phone_call_report_button)
            reportButton.setOnClickListener{
                callbacks?.onCallSelected(report)
            }

            detailFrame.visibility = View.GONE
        }

        private fun bind_Values(){
            dateTextView.setText(
                    DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(
                            phoneCall.date
                    )
            )
            numberTextView.setText(report.FormatedNumber())
            durationTextView.setText(report.duration.toString())
            typeTextView.setText(report.TypeString())
        }
    }

    private inner class ReportAdapter(var reports: List<Report>):RecyclerView.Adapter<ReportHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportHolder {
            val view = layoutInflater.inflate(viewType, parent,false)
            return ReportHolder(view)
        }

        override fun onBindViewHolder(holder: ReportHolder, position: Int) {
            holder.bind(reports[position])
        }

        override fun getItemCount(): Int = reports.count()

    }
}