package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.viewmodels.player.PlayerState

interface PlayerInterface {
    fun preparePlayer(trackUrl: String)
    fun startPlayer()
    fun pausePlayer()
    fun getCurrentPosition(): Int
    fun release()
    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)
}