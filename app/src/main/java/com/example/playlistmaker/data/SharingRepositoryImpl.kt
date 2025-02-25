package com.example.playlistmaker.data

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.navigator.ExternalNavigator
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.navigator.EmailData
import com.example.playlistmaker.domain.repository.SharingRepository
import java.text.SimpleDateFormat
import java.util.Locale

class SharingRepositoryImpl(
    private val externalNavigator: ExternalNavigator,
    val context: Context
) :
    SharingRepository {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    override fun sharePlayList(playlist: Playlist) {
        externalNavigator.shareLink(getPlayListString(playlist))
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.message)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            arrayOf(context.getString(R.string.support_address)),
            context.getString(R.string.email_theme),
            context.getString(R.string.email_message)
        )
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.offer)
    }

    private fun getPlayListString(playlist: Playlist): String {
        var text = playlist.name + "\n"
        text += playlist.description + "\n"
        text += context.resources.getQuantityString(
            R.plurals.tracksContOfList,
            playlist.trackCount.toInt(), playlist.trackCount
        ) + "\n"
        for (i in playlist.tracks.indices) {
            val duration = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(playlist.tracks[i].trackTime)
            text += "${i + 1}. ${playlist.tracks[i].artistName} - ${playlist.tracks[i].trackName}  ($duration)\n"
        }
        return text
    }
}