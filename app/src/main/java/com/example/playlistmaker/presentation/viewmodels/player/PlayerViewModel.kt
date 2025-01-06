package com.example.playlistmaker.presentation.viewmodels.player


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.states.PlayerState
import com.example.playlistmaker.domain.PlayControlInteractor
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(private val interactor: PlayControlInteractor, private val historyInteractor: HistoryInteractor): ViewModel(){

    private val DELAY_MILLIS = 300L

    private val stateLiveData = MutableLiveData(PlayerState.PREPARED)
    fun observeState(): LiveData<PlayerState> = stateLiveData

    private val stateProgressTimeLiveData = MutableLiveData<String>()
    fun observeTime(): LiveData<String> = stateProgressTimeLiveData

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

    fun prepare(url: String) {
        interactor.preparePlayer(url)
    }

    fun playbackControl() {
        val state = interactor.playbackControl()
        renderState(state)
        if (state == PlayerState.PLAYING) startTimer(state) else cancelTimer()
    }

    private fun renderState(state: PlayerState) {
        stateLiveData.postValue(state)
    }

    override fun onCleared() {
        super.onCleared()
        interactor.release()
    }

    fun onPause() {
        interactor.pausePlayer()
        renderState(PlayerState.PAUSED)
    }
}
