package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.Track

interface TrackHistoryRepository {
    fun getTrackList(): List<Track>
    fun setTrack(track: Track)
    fun clear()
}