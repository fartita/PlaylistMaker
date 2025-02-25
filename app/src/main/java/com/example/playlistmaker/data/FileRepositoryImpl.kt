package com.example.playlistmaker.data

import android.net.Uri
import com.example.playlistmaker.data.file_storage.FilePrivateStorage
import com.example.playlistmaker.domain.repository.FileRepository
import java.io.File

class FileRepositoryImpl(private val storage: FilePrivateStorage): FileRepository {
    override fun saveImage(filePath: File, savePlaylist: String, uri: Uri) {
        storage.saveImageToPrivateStorage(filePath,savePlaylist,uri)    }
}