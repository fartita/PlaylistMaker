package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.Track

interface HistoryInteractor {
    fun getTrackList(): List<Track>
    fun setTrack(track: Track)
    fun clear()
    fun getTrack(): Track
}