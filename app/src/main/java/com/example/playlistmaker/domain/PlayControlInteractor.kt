package com.example.playlistmaker.domain

import com.example.playlistmaker.data.states.PlayerState

interface PlayControlInteractor {
    fun playbackControl(): PlayerState
    fun preparePlayer(trackUrl: String)
    fun pausePlayer()
    fun release()
    fun setStateOnChangeListener(callBack: (PlayerState) -> Unit)
    fun getProgressTime(): String
}