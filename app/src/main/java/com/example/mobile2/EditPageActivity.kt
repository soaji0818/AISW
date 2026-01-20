package com.example.mobile2

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class EditPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editpage)

        // 뒤로가기
        val tvBack = findViewById<TextView>(R.id.tvBack)
        tvBack.setOnClickListener {
            finish()
        }

        // ===== 이름 =====
        val layoutName = findViewById<LinearLayout>(R.id.tvName)
        val tvNameValue = findViewById<TextView>(R.id.tvNameValue)

        layoutName.setOnClickListener {
            showEditDialog(
                title = "이름 수정",
                hint = "이름 입력",
                targetView = tvNameValue
            )
        }

        // ===== 아이디 =====
        val layoutId = findViewById<LinearLayout>(R.id.tvId)
        val tvIdValue = findViewById<TextView>(R.id.tvIdValue)

        layoutId.setOnClickListener {
            showEditDialog(
                title = "아이디 수정",
                hint = "아이디 입력",
                targetView = tvIdValue
            )
        }

        // ===== 이메일 =====
        val layoutEmail = findViewById<LinearLayout>(R.id.rowEmail)
        val tvEmailValue = findViewById<TextView>(R.id.tvEmailValue)

        layoutEmail.setOnClickListener {
            showEditDialog(
                title = "이메일 수정",
                hint = "이메일 입력",
                targetView = tvEmailValue
            )
        }

        // ===== 비밀번호 변경 =====
        val tvPassword = findViewById<TextView>(R.id.tvPassword)
        tvPassword.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("비밀번호 변경")
                .setMessage("비밀번호 변경 화면으로 이동하세요.")
                .setPositiveButton("확인", null)
                .show()
        }
    }

    // 공통 수정 다이얼로그
    private fun showEditDialog(
        title: String,
        hint: String,
        targetView: TextView
    ) {
        val editText = EditText(this).apply {
            this.hint = hint
            setText(targetView.text)
            setPadding(40, 30, 40, 30)
        }

        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(editText)
            .setPositiveButton("저장") { _, _ ->
                val newValue = editText.text.toString()
                if (newValue.isNotBlank()) {
                    targetView.text = newValue
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }
}
