package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.Track

interface OneTrackRepository {
    fun getTrack(): Track
}