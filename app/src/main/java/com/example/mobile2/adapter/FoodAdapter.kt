package com.example.mobile2.adapter

import android.app.Dialog
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile2.R
import com.example.mobile2.data.FoodItem
import com.example.mobile2.QrUtil
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class FoodAdapter(
    private val items: MutableList<FoodItem>
) : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        /* ---------------- 기본 텍스트 바인딩 ---------------- */
        holder.tvTitle.text = item.name
        holder.tvSub.text = item.category
        holder.tvCategoryDetail.text = "카테고리: ${item.category}"
        holder.tvExpireDate.text = "유통기한: ${item.expireDate}"

        /* ---------------- 유통기한 계산 ---------------- */
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val expire = LocalDate.parse(item.expireDate, formatter)
        val today = LocalDate.now()
        val remainDays = ChronoUnit.DAYS.between(today, expire)

        holder.tvRemainDays.text = "D-$remainDays"

        /* ---------------- 상태 분기 ---------------- */
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

        /* ---------------- 펼침 / 접힘 ---------------- */
        holder.detail.visibility = if (item.isExpanded) View.VISIBLE else View.GONE
        holder.arrow.text = if (item.isExpanded) "˄" else "˅"

        holder.summary.setOnClickListener {
            item.isExpanded = !item.isExpanded
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                notifyItemChanged(pos)
            }
        }

        /* ---------------- QR 보기 (팝업) ---------------- */
        holder.btnShowQr.setOnClickListener {
            showQrDialog(holder.itemView.context, item.qrText)
        }
    }

    override fun getItemCount(): Int = items.size

    /* ---------------- QR Dialog ---------------- */
    private fun showQrDialog(context: android.content.Context, qrText: String) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_qr)

        val ivQr = dialog.findViewById<ImageView>(R.id.ivQr)
        ivQr.setImageBitmap(QrUtil.makeQrBitmap(qrText))

        dialog.show()
    }
}
