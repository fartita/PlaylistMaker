package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.repository.OneTrackRepository
import com.example.playlistmaker.domain.repository.TrackHistoryRepository
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class HistoryInteractorImpl(private val oneTrackRepository: OneTrackRepository, private val historyTrackRepository: TrackHistoryRepository): HistoryInteractor {


    override fun getTrackList(): Flow<List<Track>> {
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