package com.example.mobile2.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile2.QrUtil
import com.example.mobile2.R
import com.example.mobile2.data.FoodItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class FoodAdapter : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    private val items = mutableListOf<FoodItem>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val summary: View = view.findViewById(R.id.layoutSummary)
        val detail: View = view.findViewById(R.id.layoutDetail)
        val arrow: TextView = view.findViewById(R.id.tvArrow)

        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvSub: TextView = view.findViewById(R.id.tvSub)
        val tvRemainDays: TextView = view.findViewById(R.id.tvRemainDays)
        val tvExpireDate: TextView = view.findViewById(R.id.tvExpireDate)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvCategoryDetail: TextView = view.findViewById(R.id.tvCategoryDetail)

        val btnShowQr: TextView = view.findViewById(R.id.btnShowQr)
    }

    fun submitList(list: List<FoodItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        /* ---------- 기본 정보 ---------- */
        holder.tvTitle.text = item.name
        holder.tvSub.text = item.category
        holder.tvCategoryDetail.text = "카테고리: ${item.category}"
        holder.tvExpireDate.text = "유통기한: ${item.expiryDate}"

        /* ---------- 유통기한 계산 ---------- */
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val expire = LocalDate.parse(item.expiryDate, formatter)
        val today = LocalDate.now()
        val remainDays = ChronoUnit.DAYS.between(today, expire)

        when {
            remainDays > 0 -> {
                holder.tvRemainDays.text = "D-$remainDays"
                holder.tvRemainDays.setTextColor(Color.parseColor("#2563EB"))
            }
            remainDays == 0L -> {
                holder.tvRemainDays.text = "D-DAY"
                holder.tvRemainDays.setTextColor(Color.parseColor("#F59E0B"))
            }
            else -> {
                holder.tvRemainDays.text = "D+${kotlin.math.abs(remainDays)}"
                holder.tvRemainDays.setTextColor(Color.parseColor("#DC2626"))
            }
        }

        /* ---------- 상태 ---------- */
        when {
            remainDays >= 7 -> {
                holder.tvStatus.text = "재료 상태: 안전"
                holder.tvStatus.setTextColor(Color.parseColor("#16A34A"))
            }
            remainDays >= 3 -> {
                holder.tvStatus.text = "재료 상태: 주의"
                holder.tvStatus.setTextColor(Color.parseColor("#F59E0B"))
            }
            else -> {
                holder.tvStatus.text = "재료 상태: 위험"
                holder.tvStatus.setTextColor(Color.parseColor("#DC2626"))
            }
        }

        /* ---------- 펼침 / 접힘 ---------- */
        holder.detail.visibility = if (item.isExpanded) View.VISIBLE else View.GONE
        holder.arrow.text = if (item.isExpanded) "˄" else "˅"

        holder.summary.setOnClickListener {
            item.isExpanded = !item.isExpanded
            notifyItemChanged(holder.adapterPosition)
        }

        /* ---------- QR 보기 ---------- */
        holder.btnShowQr.setOnClickListener {
            showQrDialog(holder.itemView.context, item.id)
        }
    }

    override fun getItemCount(): Int = items.size

    /* ---------- QR 다이얼로그 (정석 버전) ---------- */
    private fun showQrDialog(context: Context, foodId: Int) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.dialog_show_qr, null)

        val ivQr = view.findViewById<ImageView>(R.id.ivQr)
        ivQr.setImageBitmap(QrUtil.makeQrBitmap(foodId.toString()))

        AlertDialog.Builder(context)
            .setView(view)
            .setPositiveButton("닫기", null)
            .show()
    }
}
