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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val searchInteractor: TracksInteractor, private val historyInteractor: HistoryInteractor): ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L

    }
    private val _text = MutableStateFlow("")
    val text: StateFlow<String> = _text.asStateFlow()
    private val _stateLiveData = MutableLiveData<SearchState>()
    val stateData: LiveData<SearchState> = _stateLiveData

    private var latestSearchText: String? = null
    private var searchJob: Job? = null

    fun onTextChanged(newText: String) {
        _text.value = newText
    }

    fun searchDebounce() {
        if(text.value.isEmpty()){
            searchHistory()
            return
        }
        if (latestSearchText == text.value) {
            return
        }
        this.latestSearchText = text.value
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
            search(text.value)
        }
    }

    private val tracksList = ArrayList<Track>()

    fun repeatSearch(){
        latestSearchText?.let { search(it) }
    }


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
            searchHistory()
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
            _stateLiveData.postValue(state)
        }
}