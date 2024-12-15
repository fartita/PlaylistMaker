package com.example.playlistmaker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentMediatekBinding
import com.example.playlistmaker.presentation.recycler.LibraryViewPageAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediatekFragment: Fragment() {
    private var binding: FragmentMediatekBinding? = null
    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediatekBinding.inflate(inflater, container, false)
        return binding!!.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(binding != null){
            binding!!.viewPager.adapter = LibraryViewPageAdapter(childFragmentManager, lifecycle)
            tabMediator = TabLayoutMediator(binding!!.tabLayout, binding!!.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "Избранные треки"
                    1 -> tab.text = "Плейлисты"
                }
            }
            tabMediator.attach()
        }
    }

    override fun onDestroyView() {
        tabMediator.detach()
        binding = null
        super.onDestroyView()
    }
}