package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.domain.PlayerInterface
import com.example.playlistmaker.data.states.PlayerState

class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer)  : PlayerInterface {

    private var stateCallback: ((PlayerState) -> Unit)? = null

    override fun preparePlayer (trackUrl: String) {
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            stateCallback?.invoke(PlayerState.PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            stateCallback?.invoke(PlayerState.PREPARED)
        }
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
    override fun setOnStateChangeListener(callback: (PlayerState) -> Unit) {
        stateCallback = callback
    }
}