package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.domain.PlayControlImpl
import com.example.playlistmaker.domain.PlayerInteractor
import com.example.playlistmaker.domain.model.Track

class Player  : PlayerInteractor {
    private var mediaPlayer = MediaPlayer()


    override fun preparePlayer(
        track: Track,
        prepareCallback: () -> Unit,
        completeCallback: () -> Unit
    ) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {prepareCallback.invoke()}
        mediaPlayer.setOnCompletionListener {completeCallback.invoke()}
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }
    override fun pausePlayer() {
        mediaPlayer.pause()
    }
    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }
    override fun release() {
        mediaPlayer.release()
    }
}