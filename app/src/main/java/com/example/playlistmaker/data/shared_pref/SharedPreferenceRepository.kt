package com.example.playlistmaker.data.shared_pref

interface SharedPreferenceRepository {
    fun remove(keyName: String)
    fun putString(keyName: String, value: String)
    fun getString(keyName: String): String?
    fun getBoolean(keyName: String): Boolean
    fun putBoolean(keyName: String, value: Boolean)
}