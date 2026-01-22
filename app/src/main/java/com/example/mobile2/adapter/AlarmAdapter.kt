package com.example.mobile2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile2.R
import com.example.mobile2.data.AlarmItem

class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.VH>() {

    private val items = mutableListOf<AlarmItem>()

    fun submitList(list: List<AlarmItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alarm, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvContent = itemView.findViewById<TextView>(R.id.tvContent)
        private val tvTime = itemView.findViewById<TextView>(R.id.tvTime)

        fun bind(item: AlarmItem) {
            tvTitle.text = item.title
            tvContent.text = item.content
            tvTime.text = item.time
        }
    }
}

