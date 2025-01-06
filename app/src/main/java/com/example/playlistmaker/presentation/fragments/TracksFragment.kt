package com.example.playlistmaker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.TracksFragmentBinding
import com.example.playlistmaker.presentation.viewmodels.library.TracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TracksFragment : Fragment() {
    private val viewModel by viewModel<TracksViewModel>()
    companion object {
        fun newInstance() = TracksFragment()
    }
    private var _binding: TracksFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TracksFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}