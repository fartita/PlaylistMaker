package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<ImageView>(R.id.arrow_back_settings).setOnClickListener {
            finish()
        }

        findViewById<ImageView>(R.id.image_share).setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
                type = "text/plain"
                startActivity(Intent.createChooser(this, null))
            }
        }

        findViewById<ImageView>(R.id.image_term).setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.support_user_agreement))
                startActivity(Intent.createChooser(this, null))
            }
        }

        findViewById<ImageView>(R.id.image_support).setOnClickListener() {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_address)))
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_theme))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_message))
                startActivity(Intent.createChooser(this, null))
            }
        }
    }
}