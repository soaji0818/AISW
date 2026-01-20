package com.example.mobile2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodAdapter(
    private val items: MutableList<FoodItem>
) : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val summary: View = view.findViewById(R.id.layoutSummary)
        val detail: View = view.findViewById(R.id.layoutDetail)
        val arrow: TextView = view.findViewById(R.id.tvArrow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // 상태 반영
        holder.detail.visibility = if (item.isExpanded) View.VISIBLE else View.GONE
        holder.arrow.text = if (item.isExpanded) "˄" else "˅"

        holder.summary.setOnClickListener {
            // 상태 토글
            item.isExpanded = !item.isExpanded

            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                notifyItemChanged(pos)
            }
        }

    }

    override fun getItemCount(): Int = items.size
}
