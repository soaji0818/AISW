package com.example.mobile2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile2.R
import com.example.mobile2.data.RecipeItem

class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.VH>() {

    private val items = mutableListOf<RecipeItem>()

    fun submitList(list: List<RecipeItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        private val tvTitle = v.findViewById<TextView>(R.id.tvTitle)
        private val tvReason = v.findViewById<TextView>(R.id.tvReason)
        private val tvIngredients = v.findViewById<TextView>(R.id.tvIngredients)
        private val tvSteps = v.findViewById<TextView>(R.id.tvSteps)
        private val tvTime = v.findViewById<TextView>(R.id.tvTime)

        fun bind(item: RecipeItem) {
            tvTitle.text = item.title
            tvReason.text = item.reason
            tvIngredients.text = "재료: " + item.ingredients.joinToString(", ")
            tvSteps.text = item.steps.joinToString("\n") { "• $it" }
            tvTime.text = "예상 시간: ${item.timeMin}분"
        }
    }
}