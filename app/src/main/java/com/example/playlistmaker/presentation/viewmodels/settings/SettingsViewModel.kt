package com.example.playlistmaker.presentation.viewmodels.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.data.states.SettingsState
import com.example.playlistmaker.domain.settings.SettingsInteractor

class SettingsViewModel(private val settingsInteractor: SettingsInteractor): ViewModel() {

    private val darkTheme = MutableLiveData<Boolean>()
    val darkThemeState: LiveData<Boolean> = darkTheme

    init {
        darkTheme.postValue(settingsInteractor.getDarkTheme())
    }

    fun execute(actionType: ActionType) {
        when (actionType) {

            is ActionType.Theme -> setDarkTheme(actionType.settings)
        }
    }


    fun setDarkTheme(isDarkTheme: Boolean){
        settingsInteractor.setDarkTheme(isDarkTheme)
        darkTheme.postValue(isDarkTheme)
    }



    sealed interface ActionType {
        data class Theme( val settings: Boolean) : ActionType
    }
}