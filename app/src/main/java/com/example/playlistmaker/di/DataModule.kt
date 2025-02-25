package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.PlaylistDbConvertor
import com.example.playlistmaker.data.db.TrackDbConvertor
import com.example.playlistmaker.data.file_storage.FilePrivateStorage
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.SearchApi
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.get
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val PRACTICUM_PREFERENCES = "playlist_maker_preferences"

val dataModule = module{

    single<SearchApi>{
        Retrofit.Builder().baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SearchApi::class.java)
    }

    factory {
        androidContext()
            .getSharedPreferences(PRACTICUM_PREFERENCES, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    factory { MediaPlayer() }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "database.db"
        ).fallbackToDestructiveMigration()
            .build()
    }


    single {
        val database = get<AppDatabase>()
        database.trackDao()
    }

    single {
        val database = get<AppDatabase>()
        database.playListDao()
    }

    single{
        TrackDbConvertor()
    }

    single{
        PlaylistDbConvertor()
    }

    single{
        FilePrivateStorage(get())
    }

}