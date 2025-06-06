package com.example.playlistmaker.domain.navigator

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.playlistmaker.R

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun shareLink(shareAppLink: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(Intent.EXTRA_TEXT, shareAppLink)
        intent.type = "text/plain"
        safeStartActivity(intent)
    }
    override fun openLink(termsLink: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.data = Uri.parse(termsLink)
        safeStartActivity(intent)
    }
    override fun openEmail(supportEmailData: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, supportEmailData.emailAddressee)
        intent.putExtra(Intent.EXTRA_SUBJECT, supportEmailData.emailTopic)
        intent.putExtra(Intent.EXTRA_TEXT, supportEmailData.emailMessage)
        safeStartActivity(intent)
    }

    private fun safeStartActivity(intent: Intent) {
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                context.applicationContext,
                context.getString(R.string.error_toast_message),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}