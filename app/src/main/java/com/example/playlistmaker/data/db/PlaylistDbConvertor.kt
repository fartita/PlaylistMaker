package com.example.playlistmaker.data.db

import com.example.playlistmaker.data.db.model.PlaylistDb
import com.example.playlistmaker.domain.model.Playlist

class PlaylistDbConvertor {
    fun map(playList: Playlist): PlaylistDb {
        return PlaylistDb(
            playList.id,
            playList.name,
            playList.description,
            playList.imageUrl,
            playList.trackCount,
            playList.tracks
        )
    }
    fun map(playList: PlaylistDb): Playlist {
        return Playlist(
            playList.id,
            playList.name,
            playList.description,
            playList.imageUrl,
            playList.trackCount,
            playList.tracks
        )
    }
}