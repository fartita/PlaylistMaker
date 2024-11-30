package com.example.playlistmaker.presentation

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

import com.example.playlistmaker.domain.settings.SettingsInteractor


class App : Application() {

    private var darkTheme = false

    companion object{

        @SuppressLint("StaticFieldLeak")
        private lateinit var sInstance: Context

        fun getInstance(): Context{
            return this.sInstance
        }
    }


    private val settingsInteractor: SettingsInteractor by inject()

    override fun onCreate() {
        super.onCreate()
        sInstance = this
        startKoin {
            androidContext(this@App)
            modules(dataModule, interactorModule, repositoryModule, viewModelModule)
        }

        darkTheme = settingsInteractor.getDarkTheme()
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        settingsInteractor.setDarkTheme(darkTheme)
    }


}