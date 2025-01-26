package com.example.playlistmaker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.model.TrackDb

@Database(version = 1, entities = [TrackDb::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
}