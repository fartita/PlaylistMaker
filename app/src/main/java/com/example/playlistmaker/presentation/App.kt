package com.example.playlistmaker.presentation

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.Creator

import com.example.playlistmaker.domain.settings.SettingsInteractor


class App : Application() {

    var darkTheme = false
    private lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate() {
        settingsInteractor = Creator.getSettingInteractor(applicationContext)
        darkTheme = settingsInteractor.getDarkTheme()
        super.onCreate()
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