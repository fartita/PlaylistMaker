package com.example.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.api.SearchApi
import com.example.playlistmaker.constants.Api
import com.example.playlistmaker.constants.SharedPreference.PRACTICUM_PREFERENCES
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.model.Track
import com.example.playlistmaker.model.TrackResponse
import com.example.playlistmaker.recycler.SearchAdapter
import okhttp3.internal.toImmutableList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val SEARCH_QUERY = "SEARCH_QUERY"
    private var searchInputTextUser = ""
    private val searchHistory by lazy {
        SearchHistory(this)
    }
    private lateinit var sharedPreferences: SharedPreferences
    private val tracks = ArrayList<Track>()
    private val tracksAdapter = SearchAdapter(tracks) {
        searchHistory.setTrack(it)
        val displayIntent = Intent(this, PlayerActivity::class.java)
        startActivity(displayIntent)
    }
    private lateinit var binding: ActivitySearchBinding

    private val retrofit = Retrofit.Builder()
        .baseUrl(Api.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val serviceSearch = retrofit.create(SearchApi::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        sharedPreferences = getSharedPreferences(PRACTICUM_PREFERENCES, MODE_PRIVATE)
        
        binding.inputSearchForm.apply { 
            setOnFocusChangeListener { view, hasFocus ->
                if(searchHistory.read().isNotEmpty()){
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
            if(searchHistory.read().isNotEmpty()) binding.historySearch.visibility = View.VISIBLE
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
            searchHistory.clear(sharedPreferences)
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
                        binding.nothingWasFound.visibility = View.INVISIBLE
                        binding.networkProblem.visibility = View.INVISIBLE
                        if (searchHistory.read().isNotEmpty()) binding.historySearch.visibility =View.VISIBLE
                    }
                else{
                    binding.historySearch.visibility =View.GONE
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
        serviceSearch.searchTrack(binding.inputSearchForm.text.toString())
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>,
                ) {
                    if(response.code() == 200){
                        val result = response.body()?.results!!
                        if(result.isNotEmpty()){
                            tracks.clear()
                            tracks.addAll(result)
                            tracksAdapter.notifyDataSetChanged()
                            binding.nothingWasFound.visibility = View.INVISIBLE
                            binding.networkProblem.visibility = View.INVISIBLE
                        }
                        else{
                            showProblem(View.VISIBLE, View.INVISIBLE)
                        }
                    }
                    else{
                        showProblem(View.INVISIBLE, View.VISIBLE)
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    showProblem(View.INVISIBLE, View.VISIBLE)
                }
            })
    }

    private fun cleanList(){
        tracks.clear()
        tracksAdapter.notifyDataSetChanged()
    }

    private fun showProblem(nothingWasFoundVisible: Int, networkProblemVisible: Int){
        cleanList()
        binding.nothingWasFound.visibility = nothingWasFoundVisible
        binding.networkProblem.visibility = networkProblemVisible
    }

    private fun setHistoryList(){
        val historyTracks = ArrayList<Track>().apply {
            addAll(searchHistory.read())
        }
        binding.historySearchList.adapter = SearchAdapter(historyTracks) {
            searchHistory.setTrack(it)
            val displayIntent = Intent(this@SearchActivity, PlayerActivity::class.java)
            startActivity(displayIntent)
        }
    }
}