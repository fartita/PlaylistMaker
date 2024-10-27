package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.model.Track

interface PlayControlInteractor {
    fun playbackControl()
    fun preparePlayer(item: Track)
    fun pausePlayer()
    fun createUpdateProgressTimeRunnable(): Runnable
    fun release()
}