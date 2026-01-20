package com.example.mobile2

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etId = findViewById<EditText>(R.id.etId)
        val etPw = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvSignup = findViewById<TextView>(R.id.tvSignup)

        // 초기 상태: 비활성화
        btnLogin.isEnabled = false
        btnLogin.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor("#DADADA"))

        // 회원가입 이동
        tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        // 로그인 버튼 활성화
        fun updateLoginButton() {
            val isIdFilled = etId.text.toString().trim().isNotEmpty()
            val isPwFilled = etPw.text.toString().trim().isNotEmpty()

            if (isIdFilled && isPwFilled) {
                btnLogin.isEnabled = true
                btnLogin.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#2563EB")) // 파랑
            } else {
                btnLogin.isEnabled = false
                btnLogin.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#DADADA")) // 회색
            }
        }

        // 입력 감지
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateLoginButton()
            }

            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?, start: Int, before: Int, count: Int
            ) {}
        }

        etId.addTextChangedListener(watcher)
        etPw.addTextChangedListener(watcher)

        // 로그인 → HomeActivity
        btnLogin.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}
