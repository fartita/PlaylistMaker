package com.example.playlistmaker.presentation.viewmodels.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Creator
import com.example.playlistmaker.data.states.SettingsState
import com.example.playlistmaker.domain.settings.SettingsInteractor

class SettingsViewModel(private val settingsInteractor: SettingsInteractor): ViewModel() {

    private val stateLiveData = MutableLiveData<SettingsState>()
    fun observeState(): LiveData<SettingsState> = stateLiveData



    companion object{
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(Creator.provideSettingInteractor())
            }
        }
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