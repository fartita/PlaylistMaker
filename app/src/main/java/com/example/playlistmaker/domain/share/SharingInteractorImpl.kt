package com.example.playlistmaker.domain.share

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.repository.SharingRepository

class SharingInteractorImpl(private val sharingRepository: SharingRepository) : SharingInteractor {
    override fun shareApp() {
        sharingRepository.shareApp()
    }

    override fun openTerms() {
        sharingRepository.openTerms()
    }

    override fun openSupport() {
        sharingRepository.openSupport()
    }

    override fun sharePlayList(playlist: Playlist) {
        sharingRepository.sharePlayList(playlist)
    }

}