package com.example.playlistmaker.domain.model

data class Playlist(
    var id: Long,
    var name: String,
    var description: String,
    val imageUrl: String,
    var trackCount: Long,
    var tracks: MutableList<Track>
)
