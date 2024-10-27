package com.example.playlistmaker.domain.settings

import android.content.Context
import com.example.playlistmaker.data.shared_pref.SharedPreferenceRepository
import com.example.playlistmaker.data.shared_pref.SharedPreferenceRepositoryImp

class SettingsInteractorImpl(context: Context): SettingsInteractor {

    private val THEME_KEY = "theme_key"

    private val sharedPreferenceRepository: SharedPreferenceRepository = SharedPreferenceRepositoryImp(context)

    override fun getDarkTheme(): Boolean {
        return sharedPreferenceRepository.getBoolean(THEME_KEY)
    }

    override fun setDarkTheme(valueDarkTheme: Boolean) {
        sharedPreferenceRepository.putBoolean(THEME_KEY, valueDarkTheme)
    }
}