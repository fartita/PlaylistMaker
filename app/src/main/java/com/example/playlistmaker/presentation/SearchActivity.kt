package com.example.playlistmaker.presentation

import android.content.SharedPreferences
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
import com.example.playlistmaker.data.network.SearchApi
import com.example.playlistmaker.domain.constants.Api
import com.example.playlistmaker.domain.constants.SharedPreference.PRACTICUM_PREFERENCES
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.domain.api.TracksInteractor.TrackConsumer
import com.example.playlistmaker.presentation.recycler.SearchAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val SEARCH_QUERY = "SEARCH_QUERY"
    private var searchInputTextUser = ""

    private val SEARCH_DEBOUNCE_DELAY = 2000L
    private val CLICK_DEBOUNCE_DELAY = 1000L


    private val searchHistory by lazy {
        Creator.getHistoryRepository(this)
    }
    private lateinit var sharedPreferences: SharedPreferences
    private val interactor = Creator.provideTrackInteractor()
    private val tracksUI = ArrayList<Track>()

    private val tracksAdapter = SearchAdapter(tracksUI) {
        if(clickDebounce()){
            searchHistory.setTrack(it)
            PlayerActivity.startActivity(this)
        }
    }
    private lateinit var binding: ActivitySearchBinding

    private val retrofit = Retrofit.Builder()
        .baseUrl(Api.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val serviceSearch = retrofit.create(SearchApi::class.java)

    private val searchRunnable = Runnable { searchTrack() }
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        sharedPreferences = getSharedPreferences(PRACTICUM_PREFERENCES, MODE_PRIVATE)
        
        binding.inputSearchForm.apply { 
            setOnFocusChangeListener { view, hasFocus ->
                if(searchHistory.getTrackList().isNotEmpty()){
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
            if(searchHistory.getTrackList().isNotEmpty()) binding.historySearch.visibility = View.VISIBLE
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
            searchHistory.clear()
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
                        if (searchHistory.getTrackList().isNotEmpty()) binding.historySearch.visibility =View.VISIBLE
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
        interactor.searchTracks(binding.inputSearchForm.text.toString(), object: TrackConsumer{
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
//        serviceSearch.searchTrack(binding.inputSearchForm.text.toString())
//            .enqueue(object : Callback<TrackResponse> {
//                override fun onResponse(
//                    call: Call<TrackResponse>,
//                    response: Response<TrackResponse>,
//                ) {
//                    binding.progressBar.visibility = View.GONE
//                    if(response.code() == 200){
//                        val result = response.body()?.results!!
//                        if(result.isNotEmpty()){
//                            binding.recyclerView.visibility = View.VISIBLE
//                            tracksUI.clear()
//                            tracksUI.addAll(result)
//                            tracksAdapter.notifyDataSetChanged()
//                            binding.nothingWasFound.visibility = View.INVISIBLE
//                            binding.networkProblem.visibility = View.INVISIBLE
//                        }
//                        else{
//                            showProblem(View.VISIBLE, View.INVISIBLE)
//                        }
//                    }
//                    else{
//                        showProblem(View.INVISIBLE, View.VISIBLE)
//                    }
//                }
//
//                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
//                    showProblem(View.INVISIBLE, View.VISIBLE)
//                }
//            })
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
            addAll(searchHistory.getTrackList())
        }
        binding.historySearchList.adapter = SearchAdapter(historyTracks) {
            searchHistory.setTrack(it)
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