package com.example.playlistmaker.data.db

import com.example.playlistmaker.data.db.model.TrackDb
import com.example.playlistmaker.domain.model.Track

class TrackDbConvertor {
    fun map(track: Track, date: Long): TrackDb {
        return TrackDb(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isFavourite,
            date
        )
    }
    fun map(trackDb: TrackDb): Track {
        return Track(
            trackDb.trackId,
            trackDb.trackName,
            trackDb.artistName,
            trackDb.trackTime,
            trackDb.artworkUrl100,
            trackDb.collectionName,
            trackDb.releaseDate,
            trackDb.primaryGenreName,
            trackDb.country,
            trackDb.previewUrl,
            trackDb.isFavourite
        )
    }
}