package com.example.playlistmaker.presentation.viewmodels.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.states.FavouriteState
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.db.FavouriteTracksInteractor
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.launch

class TracksViewModel(private val interactor: FavouriteTracksInteractor, private val historyInteractor: HistoryInteractor) : ViewModel() {

    fun fill() {
        viewModelScope.launch {
            interactor.showTracks()
                .collect { tracks ->
                    processResult(tracks)
                }
        }
    }

    private val stateFavoriteLiveData = MutableLiveData<FavouriteState>()
    fun observeState(): LiveData<FavouriteState> = stateFavoriteLiveData
    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            renderState(FavouriteState.Empty)
        } else {
            renderState(FavouriteState.Content(tracks))
        }
    }
    private fun renderState(state: FavouriteState) {
        stateFavoriteLiveData.postValue(state)
    }

    fun setTrack(item:Track){
        historyInteractor.setTrack(item)
    }

}