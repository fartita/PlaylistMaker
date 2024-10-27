package com.example.playlistmaker.data

import com.example.playlistmaker.domain.model.Track

interface OneTrackRepository {
    fun getTrack(): Track
}