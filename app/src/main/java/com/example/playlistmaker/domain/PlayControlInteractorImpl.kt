package com.example.playlistmaker.domain

import com.example.playlistmaker.data.states.PlayerState

class PlayControlInteractorImpl(val mediaPlayer: PlayerInterface): PlayControlInteractor {

    private var playerState = PlayerState.PREPARED

    override fun playbackControl(): PlayerState {
        when (playerState) {
            PlayerState.PLAYING -> {
                pausePlayer()
            }
            PlayerState.PREPARED, PlayerState.PAUSED -> {
                startPlayer()
            }
        }
        return playerState
    }

    override fun preparePlayer(trackUrl: String) {
        mediaPlayer.preparePlayer(trackUrl)
    }

    private fun startPlayer() {
        mediaPlayer.startPlayer()
        playerState = PlayerState.PLAYING
    }
    override fun pausePlayer() {
        mediaPlayer.pausePlayer()
        playerState = PlayerState.PAUSED
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun setStateOnChangeListener(callBack: (PlayerState) -> Unit) {
        mediaPlayer.setOnStateChangeListener { state ->
            this.playerState = state
            callBack(state)
        }
    }

    override fun getProgressTime(): String {
        return if (playerState == PlayerState.PREPARED) TimeFormatter.ZERO_TIME else TimeFormatter.format(
            mediaPlayer.getCurrentPosition()
        )
    }
}