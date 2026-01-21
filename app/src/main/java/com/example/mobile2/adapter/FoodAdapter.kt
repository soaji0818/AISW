package com.example.mobile2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile2.R
import com.example.mobile2.data.Ingredient

class FoodAdapter : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    private val items = mutableListOf<Ingredient>()
    private val expandedPositions = mutableSetOf<Int>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val summary: View = view.findViewById(R.id.layoutSummary)
        val detail: View = view.findViewById(R.id.layoutDetail)
        val arrow: TextView = view.findViewById(R.id.tvArrow)
        val title: TextView = view.findViewById(R.id.tvTitle)
        val sub: TextView = view.findViewById(R.id.tvSub)
        val price: TextView = view.findViewById(R.id.tvPrice)
        val detailExpiry: TextView = view.findViewById(R.id.tvDetailExpiry)
        val detailStatus: TextView = view.findViewById(R.id.tvDetailStatus)
        val detailStorage: TextView = view.findViewById(R.id.tvDetailStorage)
    }

    fun submitList(list: List<Ingredient>) {
        items.clear()
        items.addAll(list)
        expandedPositions.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.title.text = item.name
        holder.sub.text = item.category
        holder.price.text = "유통기한: ${item.expiryDate}"
        holder.detailExpiry.text = "유통기한: ${item.expiryDate}"
        holder.detailStatus.text = "재료 상태: ${item.status ?: "알 수 없음"}"
        holder.detailStorage.text = "보관 방식: ${storageLabel(item.storageType)}"

        val isExpanded = expandedPositions.contains(position)
        holder.detail.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.arrow.text = if (isExpanded) "˄" else "˅"

        holder.summary.setOnClickListener {
            if (expandedPositions.contains(position)) {
                expandedPositions.remove(position)
            } else {
                expandedPositions.add(position)
            }

            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                notifyItemChanged(pos)
            }
        }

    }

    override fun getItemCount(): Int = items.size

    private fun storageLabel(storageType: String): String {
        return when (storageType.uppercase()) {
            "FREEZER" -> "냉동"
            "FRIDGE" -> "냉장"
            else -> storageType
        }
    }
}