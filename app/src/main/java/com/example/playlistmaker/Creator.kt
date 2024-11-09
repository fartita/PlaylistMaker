package com.example.playlistmaker

import com.example.playlistmaker.data.PlayerRepositoryImpl
import com.example.playlistmaker.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.shared_pref.SharedPreferenceRepositoryImp
import com.example.playlistmaker.domain.PlayControlInteractor
import com.example.playlistmaker.domain.PlayControlInteractorImpl
import com.example.playlistmaker.domain.PlayerInterface
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.HistoryInteractorImpl
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksInteractorImpl
import com.example.playlistmaker.domain.repository.TracksRepository
import com.example.playlistmaker.domain.repository.OneTrackRepository
import com.example.playlistmaker.domain.repository.SharedPreferenceRepository
import com.example.playlistmaker.domain.repository.TrackHistoryRepository
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.SettingsInteractorImpl
import com.example.playlistmaker.presentation.App

object Creator {

    fun provideTrackInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTrackRepository())
    }

    private fun getTrackRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun createPlayControl(): PlayControlInteractor {
        return PlayControlInteractorImpl(getPlayerRepository())
    }

    private fun getPlayerRepository(): PlayerInterface{
        return PlayerRepositoryImpl()
    }

    fun provideHistoryInteractor(): HistoryInteractor{
        return HistoryInteractorImpl(getOneTrackRepository(), getHistoryTrackRepository())
    }

    private fun getOneTrackRepository(): OneTrackRepository{
        return SearchHistoryRepositoryImpl(App.getInstance())
    }

    private fun getHistoryTrackRepository(): TrackHistoryRepository {
        return SearchHistoryRepositoryImpl(App.getInstance())
    }

    fun provideSettingInteractor(): SettingsInteractor{
        return SettingsInteractorImpl(getSharedRepository())
    }

    private fun getSharedRepository(): SharedPreferenceRepository {
        return SharedPreferenceRepositoryImp(App.getInstance())
    }
}