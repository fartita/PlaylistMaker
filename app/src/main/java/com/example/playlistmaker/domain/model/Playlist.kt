package com.example.playlistmaker.domain.model

data class Playlist(
    var id: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    var trackCount: Long,
    var tracks: String?
)
