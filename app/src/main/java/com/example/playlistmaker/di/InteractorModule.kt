package com.example.playlistmaker.di

import com.example.playlistmaker.data.FavouriteTracksRepositoryImpl
import com.example.playlistmaker.domain.PlayControlInteractor
import com.example.playlistmaker.domain.PlayControlInteractorImpl
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.HistoryInteractorImpl
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksInteractorImpl
import com.example.playlistmaker.domain.db.FavouriteTracksInteractor
import com.example.playlistmaker.domain.db.FavouriteTracksInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.SettingsInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    factory<HistoryInteractor>{
        HistoryInteractorImpl(get(), get())
    }

    factory<SettingsInteractor>{
        SettingsInteractorImpl(get())
    }

    factory<PlayControlInteractor>{
        PlayControlInteractorImpl(get())
    }

    factory<FavouriteTracksInteractor>{
        FavouriteTracksInteractorImpl(get())
    }

}