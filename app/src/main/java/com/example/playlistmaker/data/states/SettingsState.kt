package com.example.playlistmaker.data.states

sealed interface SettingsState {
    object Prepare: SettingsState
    data class SetDarkTheme(val isDarkTheme: Boolean):SettingsState
    data class GetDarkTheme(val isDarkTheme: Boolean):SettingsState
}