package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val search = findViewById<Button>(R.id.search)
        val mediatek = findViewById<Button>(R.id.mediatek)
        val settings = findViewById<Button>(R.id.settings)

        search.setOnClickListener(this@MainActivity)
        mediatek.setOnClickListener(this@MainActivity)
        settings.setOnClickListener(this@MainActivity)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.search -> {
                startActivity(Intent(this, SearchActivity::class.java))
            }
            R.id.mediatek -> {
                startActivity(Intent(this, MediatekActivity::class.java))
            }
            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }
    }
}