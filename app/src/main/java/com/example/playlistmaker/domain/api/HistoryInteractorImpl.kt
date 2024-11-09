package com.example.playlistmaker.domain.api

import android.content.Context
import com.example.playlistmaker.domain.repository.OneTrackRepository
import com.example.playlistmaker.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.domain.repository.TrackHistoryRepository
import com.example.playlistmaker.domain.model.Track

class HistoryInteractorImpl(private val oneTrackRepository: OneTrackRepository, private val historyTrackRepository: TrackHistoryRepository): HistoryInteractor {


    override fun getTrackList(): List<Track> {
        return historyTrackRepository.getTrackList()
    }

    override fun setTrack(track: Track) {
        historyTrackRepository.setTrack(track)
    }

    override fun clear() {
        historyTrackRepository.clear()
    }

    override fun getTrack(): Track {
        return oneTrackRepository.getTrack()
    }
}