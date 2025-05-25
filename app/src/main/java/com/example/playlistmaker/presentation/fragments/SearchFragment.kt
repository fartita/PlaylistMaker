package com.example.playlistmaker.presentation.fragments

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.data.states.SearchState
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.compose.SearchScreen
import com.example.playlistmaker.presentation.recycler.SearchAdapter
import com.example.playlistmaker.presentation.util.ConnectionBroadcastReceiver
import com.example.playlistmaker.presentation.util.debounce
import com.example.playlistmaker.presentation.viewmodels.search.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private val SEARCH_QUERY = "SEARCH_QUERY"
    private var searchInputTextUser = ""

    private val CLICK_DEBOUNCE_DELAY = 5L


    private val viewModel by viewModel<SearchViewModel>()
    private val connectionBroadcastReceiver = ConnectionBroadcastReceiver()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent { SearchScreen(
                viewModel = viewModel,
                onTrackClickDebounce = debounce(
                    CLICK_DEBOUNCE_DELAY,
                    viewLifecycleOwner.lifecycleScope,
                    false
                ) { track ->
                    viewModel.setTrack(track)
                    findNavController().navigate(
                        R.id.action_searchFragment_to_audioPlayer
                    )
                }
            ) }
        }
    }


    override fun onResume() {
        super.onResume()
        ContextCompat.registerReceiver(
            requireContext(),
            connectionBroadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION),
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(connectionBroadcastReceiver)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, searchInputTextUser)
    }




}