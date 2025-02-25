package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Duration

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {
    override fun getPlayList(playListId: Long): Flow<Playlist> {
        return repository.getPlayList(playListId)
    }

    override fun getTracks(playList: Playlist): Flow<List<Track>> = flow {
        emit(repository.getTrackList(playList.id))
    }

    override fun getPlayListTime(tracks: List<Track>): Int {
        var playListTime = 0L
        for (track in tracks){
            playListTime = track.trackTime
        }
        val duration = Duration.ofMillis(playListTime)
        val minutes = duration.toMinutes()
        return (minutes).toInt()
    }

    override suspend fun delete(playlist: Playlist) {
        repository.delete(playlist)
    }

    override suspend fun removeTrack(track: Track, playList: Playlist)  {
        repository.removeTrack(track,playList)
    }
}