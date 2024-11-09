package com.example.playlistmaker.data

import android.content.Context

import com.example.playlistmaker.data.dto.TracksDto
import com.example.playlistmaker.domain.repository.SharedPreferenceRepository
import com.example.playlistmaker.data.shared_pref.SharedPreferenceRepositoryImp
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.OneTrackRepository
import com.example.playlistmaker.domain.repository.TrackHistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class SearchHistoryRepositoryImpl(context: Context): OneTrackRepository, TrackHistoryRepository {

    companion object{
        const val MAXIMUM_SIZE = 10
    }

    private val TRACKS_KEY = "track_key"
    private val sharedPreferenceRepository: SharedPreferenceRepository = SharedPreferenceRepositoryImp(context)

    override fun getTrackList(): List<Track> {
        return read().map {
            Track(
                it.trackName,
                it.artistName,
                it.trackTime,
                it.artworkUrl100,
                it.collectionName,
                it.releaseDate,
                it.primaryGenreName,
                it.country,
                it.previewUrl
            )
        }
    }

    override fun setTrack(track: Track) {
        val tracks = read()
        val trackDto = TracksDto(
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
        if (!tracks.remove(trackDto) && tracks.size >= MAXIMUM_SIZE) tracks.removeAt(MAXIMUM_SIZE - 1)
        tracks.add(0, trackDto)
        write(tracks)
    }

    override fun clear() {
        sharedPreferenceRepository.remove(TRACKS_KEY)
    }

    private fun read(): MutableList<TracksDto> {
        val json = sharedPreferenceRepository.getString(TRACKS_KEY) ?: return mutableListOf()
        val listOfMyClassObject: Type = object : TypeToken<ArrayList<TracksDto>?>() {}.type
        return Gson().fromJson(json, listOfMyClassObject)
    }

    private fun write(tracks: MutableList<TracksDto>) {
        val json = Gson().toJson(tracks)
        sharedPreferenceRepository.putString(TRACKS_KEY, json)
    }

    override fun getTrack(): Track {
        val track = read().get(0)
        return Track(
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

}