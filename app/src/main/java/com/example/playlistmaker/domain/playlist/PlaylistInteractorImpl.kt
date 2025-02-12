package com.example.playlistmaker.domain.playlist

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {
    override fun getPlayLists(): Flow<List<Playlist>> {
        return playlistRepository.getPlayLists()
    }
    override suspend fun addTrack(context: Context, track: Track, playList: Playlist): String {
        val tracks = playlistRepository.getTrackList(playList)
        return if (tracks.isEmpty() || !tracks.contains(track)) {
            playlistRepository.addTrack(track, playList)
            context.resources.getString(R.string.add_playlist)
        } else context.resources.getString(R.string.track_in_playlist)
    }
}