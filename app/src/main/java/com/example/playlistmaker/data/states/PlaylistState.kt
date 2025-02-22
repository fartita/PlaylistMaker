package com.example.playlistmaker.data.states

import com.example.playlistmaker.domain.model.Playlist

sealed interface PlaylistState {
    data class Content(
        val items: List<Playlist>
    ) : PlaylistState
    object Empty : PlaylistState
}