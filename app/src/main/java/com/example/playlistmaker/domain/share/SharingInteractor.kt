package com.example.playlistmaker.domain.share

import com.example.playlistmaker.domain.model.Playlist

interface SharingInteractor {
    fun shareApp()
    fun openTerms()
    fun openSupport()
    fun sharePlayList(playlist: Playlist)
}