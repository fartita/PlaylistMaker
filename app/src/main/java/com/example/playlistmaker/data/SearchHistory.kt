package com.example.playlistmaker.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.data.dto.TracksDto
import com.example.playlistmaker.domain.api.OneTrackRepository
import com.example.playlistmaker.domain.api.TrackHistoryRepository
import com.example.playlistmaker.domain.constants.SharedPreference.PRACTICUM_PREFERENCES
import com.example.playlistmaker.domain.constants.SharedPreference.TRACKS_KEY
import com.example.playlistmaker.domain.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class SearchHistory(context: Context): OneTrackRepository, TrackHistoryRepository {

    companion object{
        const val MAXIMUM_SIZE = 10
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PRACTICUM_PREFERENCES,
        MODE_PRIVATE
    )

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
        sharedPreferences.edit()
            .remove(TRACKS_KEY)
            .apply()
    }

    fun read(): MutableList<TracksDto> {
        val json = sharedPreferences.getString(TRACKS_KEY, null) ?: return mutableListOf()
        val listOfMyClassObject: Type = object : TypeToken<ArrayList<TracksDto>?>() {}.type
        return Gson().fromJson(json, listOfMyClassObject)
    }

    private fun write(tracks: MutableList<TracksDto>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(TRACKS_KEY, json)
            .apply()
    }

    fun clear(sharedPreferences: SharedPreferences){
        sharedPreferences.edit()
            .remove(TRACKS_KEY)
            .apply()
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