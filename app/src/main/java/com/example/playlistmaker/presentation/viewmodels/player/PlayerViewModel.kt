package com.example.playlistmaker.presentation.viewmodels.player


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.states.PlayerState
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.db.FavouriteTracksInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.playlist.PlaylistLibraryInteractor
import com.example.playlistmaker.presentation.services.PlayerControl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
                      private val historyInteractor: HistoryInteractor,
                      private val favoriteInteractor: FavouriteTracksInteractor,
                      private val playlistLibraryInteractor: PlaylistLibraryInteractor): ViewModel(){

    private val DELAY_MILLIS = 300L

    private val stateLiveData = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observeState(): LiveData<PlayerState> = stateLiveData
    private var audioPlayerControl: PlayerControl? = null

    private val stateFavouriteData = MutableLiveData<Boolean>()
    fun observeFavouriteState(): LiveData<Boolean> = stateFavouriteData

    private val playListsLiveData = MutableLiveData<List<Playlist>>()
    fun observePlaylistState(): LiveData<List<Playlist>> = playListsLiveData

    private val addLiveData = MutableLiveData<String>()
    fun observeAddDtate(): LiveData<String> = addLiveData


    fun getTrack(): Track {
        return historyInteractor.getTrack()
    }

    fun setAudioPlayerControl(audioPlayerControl: PlayerControl) {
        this.audioPlayerControl = audioPlayerControl
        viewModelScope.launch {
            audioPlayerControl.getPlayerState().collect {
                stateLiveData.postValue(it)
            }
        }

    }

    fun showNotification(){
        audioPlayerControl?.showNotification()
    }

    fun hideNotification(){
        audioPlayerControl?.hideNotification()
    }

    fun prepare(track: Track) {
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

    fun onPlayerButtonClicked() {
        if (stateLiveData.value is PlayerState.Playing) {
            audioPlayerControl?.pausePlayer()
        } else {
            audioPlayerControl?.startPlayer()
        }
    }



    override fun onCleared() {
        super.onCleared()
        audioPlayerControl = null
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

    fun removeAudioPlayerControl() {
        audioPlayerControl = null
    }
}
