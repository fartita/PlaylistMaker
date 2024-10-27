package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.PlayerRepositoryImpl
import com.example.playlistmaker.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.PlayControlInteractor
import com.example.playlistmaker.domain.PlayControlInteractorImpl
import com.example.playlistmaker.domain.PlayerPresenter
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.HistoryInteractorImpl
import com.example.playlistmaker.data.OneTrackRepository
import com.example.playlistmaker.data.TrackHistoryRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksInteractorImpl
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.SettingsInteractorImpl

object Creator {
    private fun getTrackRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }
    fun provideTrackInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTrackRepository())
    }
    fun createPlayControl(playerPresenter: PlayerPresenter): PlayControlInteractor {
        return PlayControlInteractorImpl(PlayerRepositoryImpl(), playerPresenter)
    }

    fun getHistoryInteractor(context: Context): HistoryInteractor{
        return HistoryInteractorImpl(context)
    }

    fun getSettingInteractor(context: Context): SettingsInteractor{
        return SettingsInteractorImpl(context)
    }
}