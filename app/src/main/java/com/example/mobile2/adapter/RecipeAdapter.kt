package com.example.mobile2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
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
        private val ivImage = v.findViewById<ImageView>(R.id.ivImage)
        private val tvTitle = v.findViewById<TextView>(R.id.tvTitle)
        private val tvUsed = v.findViewById<TextView>(R.id.tvUsed)
        private val tvMissed = v.findViewById<TextView>(R.id.tvMissed)
        private val tvSource = v.findViewById<TextView>(R.id.tvSource)

        fun bind(item: RecipeItem) {
            tvTitle.text = item.title
            tvUsed.text = "사용 재료: " + item.usedIngredients.joinToString(", ")
            tvMissed.text = "부족 재료: " + item.missedIngredients.joinToString(", ")
            tvSource.text = "레시피 링크: " + (item.sourceUrl ?: "없음")

            if (item.image.isNullOrBlank()) {
                ivImage.visibility = View.GONE
            } else {
                ivImage.visibility = View.VISIBLE
                ivImage.load(item.image)
            }
        }
    }
}