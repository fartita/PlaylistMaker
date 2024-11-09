package com.example.playlistmaker.data.states

import com.example.playlistmaker.domain.model.Track

sealed interface SearchState {

    object Loading : SearchState
    object AllEmpty : SearchState

    data class Content(
        val tracks: List<Track>
    ) : SearchState

    data class EmptyInput(
        val tracks: List<Track>
    ) : SearchState


    data class Empty(
        val message: Int
    ) : SearchState
}