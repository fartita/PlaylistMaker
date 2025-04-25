package com.example.playlistmaker.di

import com.example.playlistmaker.data.FavouriteTracksRepositoryImpl
import com.example.playlistmaker.data.FileRepositoryImpl
import com.example.playlistmaker.data.PlaylistRepositoryImpl
import com.example.playlistmaker.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.SharingRepositoryImpl
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.shared_pref.SharedPreferenceRepositoryImp
import com.example.playlistmaker.domain.navigator.ExternalNavigator
import com.example.playlistmaker.domain.navigator.ExternalNavigatorImpl
import com.example.playlistmaker.domain.repository.FavouriteTracksRepository
import com.example.playlistmaker.domain.repository.FileRepository
import com.example.playlistmaker.domain.repository.OneTrackRepository
import com.example.playlistmaker.domain.repository.PlaylistRepository
import com.example.playlistmaker.domain.repository.SharedPreferenceRepository
import com.example.playlistmaker.domain.repository.SharingRepository
import com.example.playlistmaker.domain.repository.TrackHistoryRepository
import com.example.playlistmaker.domain.repository.TracksRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<ExternalNavigator>{
        ExternalNavigatorImpl(get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }

    single<OneTrackRepository>{
        SearchHistoryRepositoryImpl(get(), get(), get())
    }

    single<TrackHistoryRepository>{
        SearchHistoryRepositoryImpl(get(), get(), get())
    }

    single<SharedPreferenceRepository>{
        SharedPreferenceRepositoryImp(get())
    }

    single<FavouriteTracksRepository> {
        FavouriteTracksRepositoryImpl(get(), get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(),get(),get())
    }

    single<SharingRepository>{
        SharingRepositoryImpl(get(), get())
    }

    single<FileRepository>{
        FileRepositoryImpl(get())
    }

}