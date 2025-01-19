package com.example.playlistmaker.presentation.fragments

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.data.states.SearchState
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.PlayerActivity
import com.example.playlistmaker.presentation.recycler.SearchAdapter
import com.example.playlistmaker.presentation.viewmodels.search.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private val SEARCH_QUERY = "SEARCH_QUERY"
    private var searchInputTextUser = ""

    private val CLICK_DEBOUNCE_DELAY = 1000L


    private val viewModel by viewModel<SearchViewModel>()
    private val tracksUI = ArrayList<Track>()

    private val tracksAdapter = SearchAdapter(tracksUI) {
        if(clickDebounce()){
            viewModel.setTrack(it)
            PlayerActivity.startActivity(requireContext())
        }
    }
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var isClickAllowed = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.inputSearchForm.apply {
            setOnFocusChangeListener { view, hasFocus ->
                viewModel.searchDebounce(binding.inputSearchForm.text.toString())
            }

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(binding.inputSearchForm.text.isNotEmpty()){
                        viewModel.searchDebounce(binding.inputSearchForm.text.toString())
                    }
                    true
                }
                false
            }
        }

        binding.buttonClearSearchForm.visibility = clearButtonVisibility(binding.inputSearchForm.text)
        binding.buttonClearSearchForm.setOnClickListener {
            clearSearchForm()
            viewModel.searchDebounce("")
        }


        binding.recyclerView.adapter = tracksAdapter

        binding.buttonRefresh.setOnClickListener {
            binding.networkProblem.visibility = View.INVISIBLE
            viewModel.searchDebounce(binding.inputSearchForm.text.toString())
        }
        inputText()

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clear()
        }


    }

    private fun clearSearchForm() {
        binding.inputSearchForm.text.clear()
        val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
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

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            searchInputTextUser = savedInstanceState.getString(SEARCH_QUERY, "")
            binding.inputSearchForm.setText(searchInputTextUser)
        }

    }

    private fun inputText(){
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

                binding.buttonClearSearchForm.visibility = clearButtonVisibility(s)
                viewModel.searchDebounce(binding.inputSearchForm.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.inputSearchForm.addTextChangedListener(simpleTextWatcher)
        viewModel.observeState().observe(viewLifecycleOwner){
            render(it)
        }
    }


    private fun setHistoryList(tracks: List<Track>){
        val historyTracks = ArrayList<Track>().apply {
            addAll(tracks)
        }
        binding.historySearchList.adapter = SearchAdapter(historyTracks) {
            viewModel.setTrack(it)
            PlayerActivity.startActivity(requireActivity())
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Content -> showContent(state.tracks)
            is SearchState.Empty -> showEmpty(state.message)
            is SearchState.EmptyInput -> showHistory(state.tracks)
            is SearchState.AllEmpty -> showHistory(emptyList())
        }
    }

    private fun showLoading(){
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.historySearch.visibility = View.GONE
        binding.nothingWasFound.visibility = View.GONE
        binding.networkProblem.visibility = View.GONE
    }

    private fun showContent(tracks: List<Track>){
        tracksUI.clear()
        tracksUI.addAll(tracks)
        tracksAdapter.notifyDataSetChanged()
        binding.progressBar.visibility = View.GONE
        binding.historySearch.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.nothingWasFound.visibility = View.GONE
        binding.networkProblem.visibility = View.GONE
    }

    private fun showEmpty(message: Int){
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.historySearch.visibility = View.GONE
        if(message == R.string.nothing_found){
            binding.nothingWasFound.visibility = View.VISIBLE
            binding.networkProblem.visibility = View.GONE
        }
        else{
            binding.nothingWasFound.visibility = View.GONE
            binding.networkProblem.visibility = View.VISIBLE
        }
    }

    private fun showHistory(tracks: List<Track>){
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.nothingWasFound.visibility = View.GONE
        binding.networkProblem.visibility = View.GONE
        binding.historySearch.visibility =
            if( tracks.isNotEmpty()) { setHistoryList(tracks); View.VISIBLE;} else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}