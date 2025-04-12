package com.example.playlistmaker.di

import com.example.playlistmaker.presentation.viewmodels.activity.HostPlaylistViewModel
import com.example.playlistmaker.presentation.viewmodels.library.PlayListLibraryViewModel
import com.example.playlistmaker.presentation.viewmodels.library.OnePlayListViewModel
import com.example.playlistmaker.presentation.viewmodels.library.TracksViewModel
import com.example.playlistmaker.presentation.viewmodels.player.PlayerViewModel
import com.example.playlistmaker.presentation.viewmodels.playlist_creator.PlaylistCreatorViewModel
import com.example.playlistmaker.presentation.viewmodels.playlist_editor.PlaylistEditorViewModel
import com.example.playlistmaker.presentation.viewmodels.search.SearchViewModel
import com.example.playlistmaker.presentation.viewmodels.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get(),get())
    }

    viewModel {
        SettingsViewModel(get())
    }

    viewModel {
        PlayerViewModel(get(),get(), get(), get())
    }

    viewModel {
        PlayListLibraryViewModel(get())
    }
    viewModel {
        TracksViewModel(get(), get())
    }

    viewModel {
        PlaylistCreatorViewModel(get())
    }
    viewModel {
        PlaylistEditorViewModel(get())
    }
    viewModel{
        OnePlayListViewModel(get(), get(), get())
    }

    viewModel{
        HostPlaylistViewModel()
    }
}