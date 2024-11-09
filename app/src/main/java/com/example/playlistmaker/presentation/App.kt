package com.example.playlistmaker.presentation

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.Creator

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
    private lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate() {
        super.onCreate()
        sInstance = this
        settingsInteractor = Creator.provideSettingInteractor()
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