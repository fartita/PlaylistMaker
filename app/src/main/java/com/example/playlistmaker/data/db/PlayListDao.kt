package com.example.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.data.db.model.PlaylistDb

@Dao
interface PlayListDao {
    @Insert(entity = PlaylistDb::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playList: PlaylistDb): Long
    @Query("SELECT * FROM playlist_table")
    suspend fun getPlayLists(): List<PlaylistDb>
    @Update(entity = PlaylistDb::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlayList(playList: PlaylistDb)
    @Query("SELECT tracks FROM playlist_table WHERE id = :playListId" )
    suspend fun getTrackList(playListId: Long): List<String>
}