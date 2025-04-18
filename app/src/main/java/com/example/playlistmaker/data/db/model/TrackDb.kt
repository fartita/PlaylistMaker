package com.example.playlistmaker.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackDb(
    @PrimaryKey
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val isFavourite: Boolean,
    val date: Long,
)
