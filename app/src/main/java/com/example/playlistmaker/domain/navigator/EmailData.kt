package com.example.playlistmaker.domain.navigator

data class EmailData(
    val emailAddressee: Array<String>,
    val emailTopic: String,
    val emailMessage: String
)
