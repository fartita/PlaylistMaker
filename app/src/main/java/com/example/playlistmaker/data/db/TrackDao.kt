package com.example.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.model.TrackDb

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackDb)
    @Delete(entity = TrackDb::class)
    suspend fun delete(track: TrackDb)
    @Query("SELECT * FROM track_table WHERE trackId = :id")
    suspend fun getFavouriteTrack(id: Long): TrackDb?
    @Query("SELECT * FROM track_table ORDER BY date DESC")
    suspend fun getFavoriteTracks(): List<TrackDb>
    @Query("SELECT trackId FROM track_table")
    suspend fun getTrackId(): List<Long>
}