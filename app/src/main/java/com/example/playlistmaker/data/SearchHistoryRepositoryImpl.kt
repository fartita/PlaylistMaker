package com.example.playlistmaker.data

import android.content.Context
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.TrackDao

import com.example.playlistmaker.data.dto.TracksDto
import com.example.playlistmaker.domain.repository.SharedPreferenceRepository
import com.example.playlistmaker.data.shared_pref.SharedPreferenceRepositoryImp
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.OneTrackRepository
import com.example.playlistmaker.domain.repository.TrackHistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.reflect.Type


class SearchHistoryRepositoryImpl(private val trackDao: TrackDao, private val sharedPreferenceRepository: SharedPreferenceRepository, private val gson: Gson ): OneTrackRepository, TrackHistoryRepository {

    companion object{
        const val MAXIMUM_SIZE = 10
    }

    private val TRACKS_KEY = "track_key"

    override fun getTrackList(): Flow<List<Track>> = flow {
        emit(
            read().map{
                val favouriteTrack = trackDao.getTrackId()
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTime,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl,
                    favouriteTrack.contains(it.trackId)
                )
            }
        )
    }

    override fun setTrack(track: Track) {
        val tracks = read().toMutableList()
        tracks.find { it.trackId == track.trackId }?.isFavourite = track.isFavourite
        if (!tracks.remove(track) && tracks.size >= MAXIMUM_SIZE) tracks.removeAt(MAXIMUM_SIZE - 1)
        tracks.add(0, track)
        write(tracks)
    }

    override fun clear() {
        sharedPreferenceRepository.remove(TRACKS_KEY)
    }

    private fun read(): List<Track> {
        val json = sharedPreferenceRepository.getString(TRACKS_KEY) ?: return listOf()
        val listOfMyClassObject: Type = object : TypeToken<ArrayList<Track>?>() {}.type
        return gson.fromJson(json, listOfMyClassObject)
    }

    private fun write(tracks: MutableList<Track>) {
        val json = gson.toJson(tracks)
        sharedPreferenceRepository.putString(TRACKS_KEY, json)
    }

    override fun getTrack(): Track {
        val track = read().get(0)
        return Track(
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
            track.isFavourite
        )
    }

}