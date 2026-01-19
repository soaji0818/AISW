package com.example.appp23.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appp23.R
import com.example.appp23.data.Ingredient

class IngredientAdapter : ListAdapter<Ingredient, IngredientAdapter.VH>(DIFF) {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvMeta: TextView = itemView.findViewById(R.id.tvMeta)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        val storageKor = if (item.storageType == "FREEZER") "냉동" else "냉장"
        holder.tvName.text = item.name
        holder.tvMeta.text = "${item.category} · 유통기한 ${item.expiryDate} · $storageKor"
        holder.tvStatus.text = item.status ?: "NORMAL"
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Ingredient>() {
            override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
                return oldItem.id == newItem.id && oldItem.name == newItem.name
            }
            override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
                return oldItem == newItem
            }
        }
    }
}
