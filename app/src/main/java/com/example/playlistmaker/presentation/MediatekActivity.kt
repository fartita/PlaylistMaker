package com.example.playlistmaker.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediatekBinding
import com.example.playlistmaker.presentation.recycler.LibraryViewPageAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediatekActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMediatekBinding
    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediatekBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backToMainActivity.setOnClickListener { finish() }
        binding.viewPager.adapter = LibraryViewPageAdapter(supportFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favourites)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }
    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}