package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("search?entity=song")
    fun searchTrack (
        @Query("term") text: String
    ) : Call<TrackResponse>
}