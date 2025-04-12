package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.Playlist

interface SharingRepository {
    fun shareApp()
    fun openTerms()
    fun openSupport()
    fun sharePlayList(playlist: Playlist)
}