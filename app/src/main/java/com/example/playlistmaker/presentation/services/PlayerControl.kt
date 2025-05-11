package com.example.playlistmaker.presentation.services

import com.example.playlistmaker.data.states.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface PlayerControl {

    fun getPlayerState(): StateFlow<PlayerState>
    fun startPlayer()
    fun pausePlayer()
    fun showNotification()

    fun hideNotification()
}