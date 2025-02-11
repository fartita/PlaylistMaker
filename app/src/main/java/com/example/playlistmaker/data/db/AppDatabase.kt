package com.example.playlistmaker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.model.PlaylistDb
import com.example.playlistmaker.data.db.model.TrackDb

@Database(version = 2, entities = [TrackDb::class, PlaylistDb::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playListDao(): PlayListDao
}