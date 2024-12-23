package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.data.NetworkClient
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
}