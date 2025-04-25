package com.example.playlistmaker.presentation.viewmodels.player


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.states.PlayerState
import com.example.playlistmaker.domain.PlayControlInteractor
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.db.FavouriteTracksInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.playlist.PlaylistLibraryInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(private val interactor: PlayControlInteractor,
                      private val historyInteractor: HistoryInteractor,
                      private val favoriteInteractor: FavouriteTracksInteractor,
                      private val playlistLibraryInteractor: PlaylistLibraryInteractor): ViewModel(){

    private val DELAY_MILLIS = 300L

    private val stateLiveData = MutableLiveData(PlayerState.PREPARED)
    fun observeState(): LiveData<PlayerState> = stateLiveData

    private val stateProgressTimeLiveData = MutableLiveData<String>()
    fun observeTime(): LiveData<String> = stateProgressTimeLiveData

    private val stateFavouriteData = MutableLiveData<Boolean>()
    fun observeFavouriteState(): LiveData<Boolean> = stateFavouriteData

    private val playListsLiveData = MutableLiveData<List<Playlist>>()
    fun observePlaylistState(): LiveData<List<Playlist>> = playListsLiveData

    private val addLiveData = MutableLiveData<String>()
    fun observeAddDtate(): LiveData<String> = addLiveData

    private var timerJob: Job? = null


    private fun startTimer(state: PlayerState) {
        timerJob = viewModelScope.launch(Dispatchers.Default) {
            while (state == PlayerState.PLAYING){
                delay(DELAY_MILLIS)
                stateProgressTimeLiveData.postValue( interactor.getProgressTime())
            }
        }
    }

    private fun cancelTimer() {
        timerJob?.cancel()
        timerJob = null
    }


    init {
        interactor.setStateOnChangeListener { state->
            stateLiveData.postValue(state)
            val progressTime = interactor.getProgressTime()
            stateProgressTimeLiveData.postValue(progressTime)
            if (state == PlayerState.PREPARED) cancelTimer()
        }
    }

    fun getTrack(): Track {
        return historyInteractor.getTrack()
    }

    fun prepare(track: Track) {
        interactor.preparePlayer(track.previewUrl)
        viewModelScope.launch {
            favoriteInteractor.getTrack(track.trackId).collect{ it ->
                if(it != null){
                    renderFavouriteState(it)
                }
                else{
                    renderFavouriteState(track)
                }
            }
        }
    }

    fun playbackControl() {
        val state = interactor.playbackControl()
        renderState(state)
        if (state == PlayerState.PLAYING) startTimer(state) else cancelTimer()
    }


    override fun onCleared() {
        super.onCleared()
        interactor.release()
    }

    fun onPause() {
        interactor.pausePlayer()
        renderState(PlayerState.PAUSED)
    }

    fun onFavouriteClicked(track: Track) {
        viewModelScope.launch {
            renderFavouriteState(favoriteInteractor.updateFavorite(track))
        }
    }
    private fun renderFavouriteState(track: Track) {
        stateFavouriteData.postValue(track.isFavourite)
    }

    private fun renderToastState(message: String) {
        addLiveData.postValue(message)
    }

    fun addToPlaylist(context: Context, track: Track, playList: Playlist) {
        viewModelScope.launch {
            val message = playlistLibraryInteractor.addTrack(context, track, playList) + playList.name
            renderToastState(message)
        }
    }
    fun renderPlayLists() {
        viewModelScope.launch {
            playlistLibraryInteractor.getPlayLists()
                .collect { playLists ->
                    playListsLiveData.postValue(playLists)
                }
        }
    }

    private fun renderState(state: PlayerState) {
        stateLiveData.postValue(state)
    }
}
