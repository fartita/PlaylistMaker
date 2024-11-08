package com.example.playlistmaker.presentation.viewmodels.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Creator
import com.example.playlistmaker.data.states.PlayerState
import com.example.playlistmaker.data.states.SettingsState
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.presentation.viewmodels.search.SearchViewModel

class SettingsViewModel(): ViewModel() {

    private val stateLiveData = MutableLiveData<SettingsState>()
    fun observeState(): LiveData<SettingsState> = stateLiveData

    private lateinit var settingsInteractor: SettingsInteractor

    companion object{
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel()
            }
        }
    }

    fun prepare(context: Context){
        settingsInteractor = Creator.getSettingInteractor(context)
    }

    fun getDarkTheme(){
        renderState(SettingsState.GetDarkTheme(settingsInteractor.getDarkTheme()))
    }

    fun setDarkTheme(isDarkTheme: Boolean){
        settingsInteractor.setDarkTheme(isDarkTheme)
        renderState(SettingsState.SetDarkTheme(isDarkTheme))
    }

    fun setInit(){
        renderState(SettingsState.Prepare)
    }

    private fun renderState(state: SettingsState) {
        stateLiveData.postValue(state)
    }
}