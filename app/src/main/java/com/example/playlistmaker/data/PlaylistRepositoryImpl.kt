package com.example.playlistmaker.data

import com.example.playlistmaker.data.db.PlayListDao
import com.example.playlistmaker.data.db.PlaylistDbConvertor
import com.example.playlistmaker.data.db.model.PlaylistDb
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.PlaylistRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.reflect.Type

class PlaylistRepositoryImpl(
    private val dao: PlayListDao,
    private val gson: Gson,
    private val playListDbMapper: PlaylistDbConvertor
) : PlaylistRepository {
    override fun getPlayLists(): Flow<List<Playlist>> = flow {
        val playLists = dao.getPlayLists()
        emit(convertFromEntity(playLists))
    }
    private fun convertFromEntity(playLists: List<PlaylistDb>): List<Playlist> {
        return playLists.map { playList ->
            val tracks = convertToList(playList.tracks)
            playListDbMapper.map(playList, tracks)
        }
    }
    override suspend fun addPlaylist(playList: Playlist): Long {
        val playListEntity = convertToTrackEntity(playList)
        return dao.insertPlaylist(playListEntity)
    }
    override suspend fun addTrack(track: Track, playList: Playlist) {
        ++playList.trackCount
        playList.tracks.add(track)
        val playListEntity = convertToTrackEntity(playList)
        dao.updatePlayList(playListEntity)
    }
    override suspend fun getTrackList(playListId: Long): List<Track> {
        val tracks = dao.getTrackList(playListId).first()
        return convertToList(tracks)
    }

    override fun getPlayList(playListId: Long): Flow<Playlist> = flow {
        val playListDb = dao.getPlayList(playListId).first()
        emit(convertPayListFromEntity(playListDb))
    }

    override suspend fun delete(playlist: Playlist) {
        val playListEntity = convertToTrackEntity(playlist)
        dao.delete(playListEntity)
    }

    override suspend fun removeTrack(track: Track, playList: Playlist) {--playList.trackCount
        playList.tracks.remove(track)
        val playListEntity = convertToTrackEntity(playList)
        dao.updatePlayList(playListEntity)
    }

    override suspend fun updatePlayList(playList: Playlist) {
        val playListEntity = convertToTrackEntity(playList)
        dao.updatePlayList(playListEntity)
    }

    private fun convertToTrackEntity(playList: Playlist): PlaylistDb {
        val tracks = convertFromList(playList.tracks)
        return playListDbMapper.map(playList, tracks)
    }

    private fun convertPayListFromEntity(playList: PlaylistDb): Playlist {
        val tracks = convertToList(playList.tracks)
        return playListDbMapper.map(playList,tracks)
    }

    private fun convertFromList(tracks: MutableList<Track>): String {
        return gson.toJson(tracks)
    }

    private fun convertToList(jsonList: String?): MutableList<Track> {
        val listOfMyClassObject: Type = object : TypeToken<ArrayList<Track>?>() {}.type
        return if (!jsonList.isNullOrEmpty()) gson.fromJson(jsonList, listOfMyClassObject) else mutableListOf()
    }

}