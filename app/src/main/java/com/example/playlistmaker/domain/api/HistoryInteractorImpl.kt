package com.example.playlistmaker.domain.api

import android.content.Context
import com.example.playlistmaker.data.OneTrackRepository
import com.example.playlistmaker.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.TrackHistoryRepository
import com.example.playlistmaker.domain.model.Track

class HistoryInteractorImpl(context: Context): HistoryInteractor {

    private val oneTrackRepository: OneTrackRepository = SearchHistoryRepositoryImpl(context)
    private val historyTrackRepository: TrackHistoryRepository = SearchHistoryRepositoryImpl(context)

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