package com.example.playlistmaker.presentation.viewmodels.player

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.data.states.PlayerState
import com.example.playlistmaker.domain.PlayControlInteractor
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.HistoryInteractorImpl
import com.example.playlistmaker.domain.model.Track

class PlayerViewModel(private val interactor: PlayControlInteractor, private val historyInteractor: HistoryInteractor): ViewModel(){

    private val DELAY_MILLIS = 350L

    private val stateLiveData = MutableLiveData(PlayerState.PREPARED)
    fun observeState(): LiveData<PlayerState> = stateLiveData

    private val stateProgressTimeLiveData = MutableLiveData<String>()
    fun observeTime(): LiveData<String> = stateProgressTimeLiveData

    private var mainThreadHandler = Handler(Looper.getMainLooper())

    private val progressTimeRunnable = object : Runnable {
        override fun run() {
            val progressTime = interactor.getProgressTime()
            stateProgressTimeLiveData.postValue(progressTime)
            mainThreadHandler.postDelayed(this, DELAY_MILLIS)
        }
    }


    init {
        interactor.setStateOnChangeListener { state->
            stateLiveData.postValue(state)
            val progressTime = interactor.getProgressTime()
            stateProgressTimeLiveData.postValue(progressTime)
            if (state == PlayerState.PREPARED) mainThreadHandler.removeCallbacks(
                progressTimeRunnable
            )
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
        if (state == PlayerState.PLAYING) mainThreadHandler.post(progressTimeRunnable) else mainThreadHandler.removeCallbacks(
            progressTimeRunnable
        )
    }

    private fun renderState(state: PlayerState) {
        stateLiveData.postValue(state)
    }

    override fun onCleared() {
        super.onCleared()
        interactor.release()
        mainThreadHandler.removeCallbacks(progressTimeRunnable)
    }

    fun onPause() {
        interactor.pausePlayer()
        renderState(PlayerState.PAUSED)
    }
}
