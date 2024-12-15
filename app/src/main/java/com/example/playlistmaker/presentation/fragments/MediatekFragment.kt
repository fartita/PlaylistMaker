package com.example.playlistmaker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediatekBinding
import com.example.playlistmaker.presentation.recycler.LibraryViewPageAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediatekFragment: Fragment() {
    private var _binding: FragmentMediatekBinding? = null
    private val binding get() = _binding!!

    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediatekBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            binding.viewPager.adapter = LibraryViewPageAdapter(childFragmentManager, lifecycle)
            tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.favourites)
                    1 -> tab.text = getString(R.string.playlists)
                }
            }
            tabMediator.attach()
    }

    override fun onDestroyView() {
        tabMediator.detach()
        super.onDestroyView()
        _binding = null
    }
}