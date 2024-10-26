package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.model.Track

class PlayControlImpl(val mediaPlayer: PlayerInteractor, val playerPresenter: PlayerPresenter): PlayControl {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
    private var playerState = STATE_DEFAULT
    private fun setPlayerState() {
        playerState = STATE_PREPARED
    }
    private fun setPlayerStatePrepared() {
        playerState = STATE_PREPARED
        playerPresenter.playButtonEnabled()
    }
    override fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun preparePlayer(item: Track) {
        mediaPlayer.preparePlayer(item, this::setPlayerStatePrepared, this::setPlayerState)
    }

    private fun startPlayer() {
        mediaPlayer.startPlayer()
        playerPresenter.startPlayer()
        playerState = STATE_PLAYING
    }
    override fun pausePlayer() {
        mediaPlayer.pausePlayer()
        playerPresenter.pausePlayer()
        playerState = STATE_PAUSED
    }
    override fun createUpdateProgressTimeRunnable(): Runnable {
        return object : Runnable {
            override fun run() {
                when (playerState) {
                    STATE_PLAYING -> {
                        playerPresenter.progressTimeViewUpdate(TimeFormatter.format(mediaPlayer.getCurrentPosition()))
                        playerPresenter.postDelayed(this)
                    }
                    STATE_PAUSED -> {
                        playerPresenter.removeCallbacks(this)
                    }
                    STATE_PREPARED -> {
                        playerPresenter.pausePlayer()
                        playerPresenter.progressTimeViewUpdate(TimeFormatter.ZERO_TIME)
                        playerPresenter.removeCallbacks(this)
                    }
                }
            }
        }
    }
    override fun release() {
        mediaPlayer.release()
    }
}