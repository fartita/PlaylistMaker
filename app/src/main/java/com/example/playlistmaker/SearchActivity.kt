package com.example.playlistmaker

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
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.model.Track
import com.example.playlistmaker.model.TrackResponse
import com.example.playlistmaker.recycler.SearchAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
        private val tracks = ArrayList<Track>()
    }

    private var searchInputTextUser = ""


    private lateinit var tracksAdapter: SearchAdapter
    private lateinit var binding: ActivitySearchBinding

    private val retrofit = Retrofit.Builder()
        .baseUrl(Api.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val serviceSearch = retrofit.create(SearchApi::class.java)

    private var searchText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.inputSearchForm.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrack()
                true
            }
            false
        }
        binding.buttonClearSearchForm.visibility = clearButtonVisibility(binding.inputSearchForm.text)
        binding.buttonClearSearchForm.setOnClickListener {
            clearSearchForm()
        }

        binding.arrowBackSearch.setOnClickListener {
            finish()
        }

        binding.recyclerView.adapter = SearchAdapter(tracks, this)

        binding.buttonRefresh.setOnClickListener {
            binding.networkProblem.visibility = View.INVISIBLE
            searchTrack()
        }
        inputText()
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
                    if (searchText.isNotEmpty() && !response.body()?.results.isNullOrEmpty() && response.code() == Api.SUCCESS_CODE) {
                        tracks.clear()
                        tracks.addAll(response.body()?.results!!)
                        tracksAdapter.notifyDataSetChanged()
                        binding.nothingWasFound.visibility = View.INVISIBLE
                        binding.networkProblem.visibility = View.INVISIBLE
                    } else {
                        binding.nothingWasFound.visibility = View.VISIBLE
                        binding.networkProblem.visibility = View.INVISIBLE
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    binding.nothingWasFound.visibility = View.INVISIBLE
                    binding.networkProblem.visibility = View.VISIBLE
                }
            })
    }
}