package com.example.playlistmaker.di

import com.example.playlistmaker.presentation.viewmodels.player.PlayerViewModel
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
        PlayerViewModel(get(),get())
    }

}