package com.example.playlistmaker.data.shared_pref

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.domain.repository.SharedPreferenceRepository

class SharedPreferenceRepositoryImp(context: Context): SharedPreferenceRepository {

    private val PRACTICUM_PREFERENCES = "playlist_maker_preferences"

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PRACTICUM_PREFERENCES,
        MODE_PRIVATE
    )

    override fun remove(keyName: String) {
        sharedPreferences.edit()
            .remove(keyName)
            .apply()
    }

    override fun putString(keyName: String, value: String) {
        sharedPreferences.edit()
            .putString(keyName, value)
            .apply()
    }

    override fun getString(keyName: String): String? {
        return sharedPreferences.getString(keyName, null)
    }

    override fun getBoolean(keyName: String): Boolean {
        return sharedPreferences.getBoolean(keyName, false)
    }

    override fun putBoolean(keyName: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(keyName, value).apply()
    }


}