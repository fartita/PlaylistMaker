package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.Track

interface OneTrackRepository {
    fun getTrack(): Track
}