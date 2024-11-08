package com.example.playlistmaker.domain

import com.example.playlistmaker.data.states.PlayerState

interface PlayerInterface {
    fun preparePlayer(trackUrl: String)
    fun startPlayer()
    fun pausePlayer()
    fun getCurrentPosition(): Int
    fun release()
    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)
}