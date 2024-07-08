package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

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

    override fun onClick(buttonView: View?) {
        when (buttonView?.id) {
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