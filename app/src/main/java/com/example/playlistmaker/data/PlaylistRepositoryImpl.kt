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
        return playLists.map { playList -> playListDbMapper.map(playList) }
    }
    override suspend fun addPlaylist(playList: Playlist): Long {
        val playListEntity = convertToTrackEntity(playList)
        return dao.insertPlaylist(playListEntity)
    }
    override suspend fun addTrack(track: Track, playList: Playlist) {
        ++playList.trackCount
        playList.tracks = setTrack(track, playList.tracks)
        val playListEntity = convertToTrackEntity(playList)
        dao.updatePlayList(playListEntity)
    }
    override suspend fun getTrackList(playList: Playlist): List<Track> {
        val tracks = dao.getTrackList(playList.id).first()
        return convertToList(tracks)
    }
    private fun convertToTrackEntity(playList: Playlist): PlaylistDb {
        return playListDbMapper.map(playList)
    }

    private fun convertToList(jsonList: String?): MutableList<Track> {
        val listOfMyClassObject: Type = object : TypeToken<ArrayList<Track>?>() {}.type
        return if (!jsonList.isNullOrEmpty()) gson.fromJson(jsonList, listOfMyClassObject) else mutableListOf()
    }
    private fun setTrack(track: Track, jsonList: String?): String {
        val tracks = convertToList(jsonList)
        tracks.add(0, track)
        return write(tracks)
    }
    private fun write(tracks: MutableList<Track>): String {
        return gson.toJson(tracks)
    }
}