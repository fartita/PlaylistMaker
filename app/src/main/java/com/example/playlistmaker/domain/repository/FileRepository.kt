package com.example.playlistmaker.domain.repository

import android.net.Uri
import java.io.File

interface FileRepository {
    fun saveImage(filePath: File, savePlaylist: String, uri: Uri)
}