package com.example.mobile2

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import android.content.res.ColorStateList
import android.graphics.Color

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // ===== View 연결 =====
        val etName = findViewById<EditText>(R.id.etName)
        val etId = findViewById<EditText>(R.id.etId)
        val etEmailId = findViewById<EditText>(R.id.etEmailId)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etPasswordConfirm = findViewById<EditText>(R.id.etPasswordConfirm)
        val spinner = findViewById<Spinner>(R.id.email_domains)
        val btnSignUp = findViewById<Button>(R.id.btnSignUpComplete)

        // ===== 이메일 도메인 Spinner =====
        ArrayAdapter.createFromResource(
            this,
            R.array.email_domains,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        // ===== 버튼 초기 상태 (회색 + 비활성) =====
        btnSignUp.isEnabled = false
        btnSignUp.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor("#D1D5DB"))

        // ===== 버튼 상태 업데이트 함수 =====
        fun updateSignUpButton() {
            val isAllFilled =
                etName.text.isNotBlank() &&
                        etId.text.isNotBlank() &&
                        etEmailId.text.isNotBlank() &&
                        etPassword.text.isNotBlank() &&
                        etPasswordConfirm.text.isNotBlank()

            btnSignUp.isEnabled = isAllFilled

            if (isAllFilled) {
                // 파랑
                btnSignUp.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#2563EB"))
            } else {
                // 회색
                btnSignUp.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#D1D5DB"))
            }
        }

        // ===== 공통 TextWatcher =====
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateSignUpButton()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        // ===== 모든 입력칸에 등록 =====
        etName.addTextChangedListener(watcher)
        etId.addTextChangedListener(watcher)
        etEmailId.addTextChangedListener(watcher)
        etPassword.addTextChangedListener(watcher)
        etPasswordConfirm.addTextChangedListener(watcher)

        // ===== 가입 완료 버튼 클릭 =====
        btnSignUp.setOnClickListener {

            val email =
                etEmailId.text.toString() + spinner.selectedItem.toString()

            // TODO: 서버 회원가입 처리 (name, id, email, password)

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
