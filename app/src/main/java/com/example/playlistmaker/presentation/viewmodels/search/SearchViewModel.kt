package com.example.playlistmaker.presentation.viewmodels.search

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.model.Track
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.R
import com.example.playlistmaker.data.states.SearchState

class SearchViewModel(private val searchInteractor: TracksInteractor, private val historyInteractor: HistoryInteractor): ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

    }
    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private var latestSearchText: String? = null
    private val handler = Handler(Looper.getMainLooper())

    fun searchDebounce(changedText: String) {
        if(changedText.isEmpty()){
            searchHistory()
        }
        else{
            if (latestSearchText == changedText) {
                return
            }
            this.latestSearchText = changedText
            handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

            val searchRunnable = Runnable { search(changedText) }

            val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY_MILLIS
            handler.postAtTime(
                searchRunnable,
                SEARCH_REQUEST_TOKEN,
                postTime,
            )
        }

    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    private val tracksList = ArrayList<Track>()


        private fun searchHistory() {
            val history = historyInteractor.getTrackList()
            if (history.isNotEmpty())
                renderState(
                    SearchState.EmptyInput(
                        history
                    )
                ) else renderState(
                SearchState.AllEmpty
            )
        }

        fun setTrack(track: Track) {
            historyInteractor.setTrack(track)
        }

        fun clear() {
            historyInteractor.clear()
            renderState(
                SearchState.AllEmpty)
        }


        private fun search(newSearchText: String) {
            renderState(
                    SearchState.Loading
                )
                searchInteractor.searchTracks(
                    newSearchText, object: TracksInteractor.TrackConsumer{
                        override fun consume(tracks: List<Track>?) {
                            handler.post {
                                if(tracks != null){
                                    tracksList.clear()
                                    tracksList.addAll(tracks)
                                }
                                if(tracks == null){
                                    renderState(SearchState.Empty(R.string.network_problem))
                                }
                                else if(tracks.isEmpty()){
                                    renderState(SearchState.Empty(R.string.nothing_found))
                                }
                                else{
                                    renderState(SearchState.Content(tracksList))
                                }
                            }

                        }
                    }
                )
        }

        private fun renderState(state: SearchState) {
            stateLiveData.postValue(state)
        }
}