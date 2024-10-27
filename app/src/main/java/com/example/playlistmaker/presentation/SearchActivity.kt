package com.example.playlistmaker.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.api.TracksInteractor.TrackConsumer
import com.example.playlistmaker.presentation.recycler.SearchAdapter

class SearchActivity : AppCompatActivity() {

    private val SEARCH_QUERY = "SEARCH_QUERY"
    private var searchInputTextUser = ""

    private val SEARCH_DEBOUNCE_DELAY = 2000L
    private val CLICK_DEBOUNCE_DELAY = 1000L


    private val historyInteractor by lazy {
        Creator.getHistoryInteractor(this)
    }
    private val tracksInteractor = Creator.provideTrackInteractor()
    private val tracksUI = ArrayList<Track>()

    private val tracksAdapter = SearchAdapter(tracksUI) {
        if(clickDebounce()){
            historyInteractor.setTrack(it)
            PlayerActivity.startActivity(this)
        }
    }
    private lateinit var binding: ActivitySearchBinding

    private val searchRunnable = Runnable { searchTrack() }
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        
        binding.inputSearchForm.apply { 
            setOnFocusChangeListener { view, hasFocus ->
                if(historyInteractor.getTrackList().isNotEmpty()){
                    binding.historySearch.visibility =
                        if (hasFocus && binding.inputSearchForm.text.isEmpty()) View.VISIBLE else View.GONE
                    setHistoryList()
                }
            }

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchTrack()
                    true
                }
                false
            }
        }

        binding.buttonClearSearchForm.visibility = clearButtonVisibility(binding.inputSearchForm.text)
        binding.buttonClearSearchForm.setOnClickListener {
            clearSearchForm()
            cleanList()
            if(historyInteractor.getTrackList().isNotEmpty()) binding.historySearch.visibility = View.VISIBLE
        }

        binding.arrowBackSearch.setOnClickListener {
            finish()
        }

        binding.recyclerView.adapter = tracksAdapter

        binding.buttonRefresh.setOnClickListener {
            binding.networkProblem.visibility = View.INVISIBLE
            searchTrack()
        }
        inputText()

        binding.clearHistoryButton.setOnClickListener {
            historyInteractor.clear()
            setHistoryList()
            binding.historySearch.visibility = View.INVISIBLE
        }
    }

    private fun clearSearchForm() {
        binding.inputSearchForm.text.clear()

        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, searchInputTextUser)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchInputTextUser = savedInstanceState.getString(SEARCH_QUERY, "")
        binding.inputSearchForm.setText(searchInputTextUser)
    }

    private fun inputText(){
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.buttonClearSearchForm.visibility = clearButtonVisibility(s)
                searchInputTextUser = binding.inputSearchForm.text.toString()
                if (binding.inputSearchForm.hasFocus() && s?.isEmpty() == true){
                        handler.removeCallbacks(searchRunnable)
                        binding.nothingWasFound.visibility = View.INVISIBLE
                        binding.networkProblem.visibility = View.INVISIBLE
                        if (historyInteractor.getTrackList().isNotEmpty()) binding.historySearch.visibility =View.VISIBLE
                    }
                else{
                    binding.historySearch.visibility =View.GONE
                    searchDebounce()
                }
                setHistoryList()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.inputSearchForm.addTextChangedListener(simpleTextWatcher)
    }

    private fun searchTrack() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.nothingWasFound.visibility = View.GONE
        binding.networkProblem.visibility = View.GONE
        tracksInteractor.searchTracks(binding.inputSearchForm.text.toString(), object: TrackConsumer{
            override fun consume(tracks: List<Track>) {
                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    tracksUI.clear()
                    if (tracks.isNotEmpty()) {
                        binding.recyclerView.visibility = View.VISIBLE
                        tracksUI.clear()
                        tracksUI.addAll(tracks)
                        tracksAdapter.notifyDataSetChanged()
                        binding.nothingWasFound.visibility = View.INVISIBLE
                        binding.networkProblem.visibility = View.INVISIBLE
                    } else {
                        showProblem(View.VISIBLE, View.INVISIBLE)
                    }
                }

            }
        })
    }

    private fun cleanList(){
        tracksUI.clear()
        tracksAdapter.notifyDataSetChanged()
    }

    private fun showProblem(nothingWasFoundVisible: Int, networkProblemVisible: Int){
        cleanList()
        binding.nothingWasFound.visibility = nothingWasFoundVisible
        binding.networkProblem.visibility = networkProblemVisible
    }

    private fun setHistoryList(){
        val historyTracks = ArrayList<Track>().apply {
            addAll(historyInteractor.getTrackList())
        }
        binding.historySearchList.adapter = SearchAdapter(historyTracks) {
            historyInteractor.setTrack(it)
            PlayerActivity.startActivity(this)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }
}