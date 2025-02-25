package com.example.playlistmaker.data.db

import com.example.playlistmaker.data.db.model.PlaylistDb
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track

class PlaylistDbConvertor {
    fun map(playList: Playlist, tracks: String): PlaylistDb {
        return PlaylistDb(
            playList.id,
            playList.name,
            playList.description,
            playList.imageUrl,
            playList.trackCount,
            tracks
        )
    }
    fun map(playList: PlaylistDb, tracks: MutableList<Track>): Playlist {
        return Playlist(
            playList.id,
            playList.name,
            playList.description,
            playList.imageUrl,
            playList.trackCount,
            tracks
        )
    }
}