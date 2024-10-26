package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.Player
import com.example.playlistmaker.data.SearchHistory
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.PlayControl
import com.example.playlistmaker.domain.PlayControlImpl
import com.example.playlistmaker.domain.PlayerPresenter
import com.example.playlistmaker.domain.api.OneTrackRepository
import com.example.playlistmaker.domain.api.TrackHistoryRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksInteractorImpl
import com.example.playlistmaker.domain.api.TracksRepository

object Creator {
    private fun getTrackRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }
    fun provideTrackInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTrackRepository())
    }
    fun createPlayControl(playerPresenter: PlayerPresenter): PlayControl {
        return PlayControlImpl(Player(), playerPresenter)
    }
    fun getOneTrackRepository(context: Context): OneTrackRepository {
        return SearchHistory(context)
    }
    fun getHistoryRepository(context: Context): TrackHistoryRepository {
        return SearchHistory(context)
    }
}