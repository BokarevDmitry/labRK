package com.example.labrk

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.labrk.cryptoApi.Record
import java.sql.Date
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class Adapter(val items: List<Record>, val callback: Callback) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var date = itemView.findViewById<TextView>(R.id.time)
        private var high = itemView.findViewById<TextView>(R.id.high)
        private var low  = itemView.findViewById<TextView>(R.id.low)

        @SuppressLint("SimpleDateFormat")
        fun bind(item: Record) {
            val dateValue = SimpleDateFormat("dd-MM-yyyy").format(SimpleDateFormat("yyyy-MM-dd").parse(Date(item.time*1000).toString()))
            val highValue = DecimalFormat("#0.00").format(item.high).toString()
            val lowValue  = DecimalFormat("#0.00").format(item.low).toString()
            date.text = dateValue
            high.text = highValue
            low.text  = lowValue
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    callback.onItemClicked(items[adapterPosition])
            }
        }
    }

    interface Callback {
        fun onItemClicked(item: Record)
    }
}