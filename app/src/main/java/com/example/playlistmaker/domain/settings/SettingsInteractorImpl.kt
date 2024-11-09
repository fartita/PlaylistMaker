package com.example.playlistmaker.domain.settings

import android.content.Context
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.repository.SharedPreferenceRepository
import com.example.playlistmaker.data.shared_pref.SharedPreferenceRepositoryImp

class SettingsInteractorImpl(private val sharedPreferenceRepository: SharedPreferenceRepository): SettingsInteractor {

    private val THEME_KEY = "theme_key"

    override fun getDarkTheme(): Boolean {
        return sharedPreferenceRepository.getBoolean(THEME_KEY)
    }

    override fun setDarkTheme(valueDarkTheme: Boolean) {
        sharedPreferenceRepository.putBoolean(THEME_KEY, valueDarkTheme)
    }
}