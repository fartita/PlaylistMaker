package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.model.Track

interface PlayerInteractor {
    fun preparePlayer(track: Track, prepareCallback: () -> Unit, completeCallback: () -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun getCurrentPosition(): Int
    fun release()
}