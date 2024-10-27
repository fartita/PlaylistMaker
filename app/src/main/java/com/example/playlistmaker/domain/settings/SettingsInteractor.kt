package com.example.playlistmaker.domain.settings

interface SettingsInteractor {
    fun getDarkTheme(): Boolean
    fun setDarkTheme(valueDarkTheme: Boolean)

}