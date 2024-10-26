package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TrackConsumer)
    interface TrackConsumer {
        fun consume(tracks: List<Track>)
    }
}