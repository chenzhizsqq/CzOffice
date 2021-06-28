package com.example.etoffice

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun login(view: View) {

        val intent: Intent = Intent(this@LoginActivity,
                MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}