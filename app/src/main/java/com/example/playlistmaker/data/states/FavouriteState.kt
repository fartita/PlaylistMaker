package com.example.playlistmaker.data.states

import com.example.playlistmaker.domain.model.Track

sealed interface FavouriteState {
    data class Content(
        val tracks: List<Track>
    ) : FavouriteState
    object Empty : FavouriteState
}