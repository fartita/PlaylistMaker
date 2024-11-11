package com.example.playlistmaker.di

import com.example.playlistmaker.data.PlayerRepositoryImpl
import com.example.playlistmaker.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.shared_pref.SharedPreferenceRepositoryImp
import com.example.playlistmaker.domain.PlayerInterface
import com.example.playlistmaker.domain.repository.OneTrackRepository
import com.example.playlistmaker.domain.repository.SharedPreferenceRepository
import com.example.playlistmaker.domain.repository.TrackHistoryRepository
import com.example.playlistmaker.domain.repository.TracksRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single<OneTrackRepository>{
        SearchHistoryRepositoryImpl(get(), get())
    }

    single<TrackHistoryRepository>{
        SearchHistoryRepositoryImpl(get(), get())
    }

    single<SharedPreferenceRepository>{
        SharedPreferenceRepositoryImp(get())
    }

    single<PlayerInterface>{
        PlayerRepositoryImpl(get())
    }

}