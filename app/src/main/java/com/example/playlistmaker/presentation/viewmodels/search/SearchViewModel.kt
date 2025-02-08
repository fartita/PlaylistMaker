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
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.data.states.SearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val searchInteractor: TracksInteractor, private val historyInteractor: HistoryInteractor): ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L

    }
    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private var latestSearchText: String? = null
    private var searchJob: Job? = null

    fun searchDebounce(changedText: String) {
        if(changedText.isEmpty()){
            searchHistory()
        }
        else{
            if (latestSearchText == changedText) {
                return
            }
            this.latestSearchText = changedText


            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
                search(changedText)
            }
        }

    }

    private val tracksList = ArrayList<Track>()


        private fun searchHistory() {
            viewModelScope.launch{
                historyInteractor.getTrackList().collect{ history ->
                    if (history.isNotEmpty())
                        renderState(
                            SearchState.EmptyInput(
                                history
                            )
                        ) else renderState(
                        SearchState.AllEmpty
                    )
                }
            }
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
            viewModelScope.launch {
                searchInteractor
                    .searchTracks(newSearchText)
                    .collect{ tracks ->
                        run {
                            if (tracks != null) {
                                tracksList.clear()
                                tracksList.addAll(tracks)
                            }
                            if (tracks == null) {
                                renderState(SearchState.Empty(R.string.network_problem))
                            } else if (tracks.isEmpty()) {
                                renderState(SearchState.Empty(R.string.nothing_found))
                            } else {
                                renderState(SearchState.Content(tracksList))
                            }
                        }
                }
            }
        }

        private fun renderState(state: SearchState) {
            stateLiveData.postValue(state)
        }
}