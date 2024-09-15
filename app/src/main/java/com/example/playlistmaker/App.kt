package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.constants.SharedPreference.PRACTICUM_PREFERENCES
import com.example.playlistmaker.constants.SharedPreference.THEME_KEY


class App : Application() {

    var darkTheme = false
    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreate() {
        sharedPreferences = getSharedPreferences(PRACTICUM_PREFERENCES,MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(THEME_KEY, darkTheme)
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
        sharedPreferences.edit().putBoolean(THEME_KEY, darkTheme).apply()
    }
}