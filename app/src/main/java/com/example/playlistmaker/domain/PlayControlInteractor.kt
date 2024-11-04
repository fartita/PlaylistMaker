package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.viewmodels.player.PlayerState

interface PlayControlInteractor {
    fun playbackControl(): PlayerState
    fun preparePlayer(trackUrl: String)
    fun pausePlayer()
    fun release()
    fun setStateOnChangeListener(callBack: (PlayerState) -> Unit)
    fun getProgressTime(): String
}