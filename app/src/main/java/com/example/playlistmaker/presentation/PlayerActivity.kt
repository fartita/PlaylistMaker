package com.example.playlistmaker.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.data.states.PlayerState
import com.example.playlistmaker.presentation.viewmodels.player.PlayerViewModel
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity: AppCompatActivity() {

    companion object {

        fun startActivity(context: Context) {
            val intent = Intent(context, PlayerActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var playButton: ImageButton
    private lateinit var progressTimeView: TextView

    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        viewModel = ViewModelProvider(this, PlayerViewModel.getViewModelFactory())[PlayerViewModel::class.java]

        playButton = findViewById(R.id.playButton)
        progressTimeView = findViewById(R.id.progressTime)

        val name = findViewById<TextView>(R.id.title)
        val artist = findViewById<TextView>(R.id.artist)
        val duration = findViewById<TextView>(R.id.trackTime)
        val album = findViewById<TextView>(R.id.albumName)
        val year = findViewById<TextView>(R.id.year)
        val genre = findViewById<TextView>(R.id.styleName)
        val country = findViewById<TextView>(R.id.countryName)
        val artwork = findViewById<ImageView>(R.id.cover)




        playButton.setOnClickListener {
            viewModel.playbackControl()
        }

        val imageBack = findViewById<ImageView>(R.id.backButton)
        imageBack.setOnClickListener { finish() }
        val item =  viewModel.getTrack(applicationContext)


        name.text = item.trackName
        artist.text = item.artistName
        album.text = item.collectionName
        year.text = item.releaseDate.substring(0,4)
        genre.text = item.primaryGenreName
        country.text = item.country
        duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTime)


        Glide.with(applicationContext)
            .load(item.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .placeholder(R.drawable.ic_track)
            .centerCrop()
            .transform(RoundedCorners(applicationContext.resources.getDimensionPixelSize(R.dimen.margin_8)))
            .into(artwork)

        viewModel.observeState().observe(this){
            render(it)
        }

        viewModel.observeTime().observe(this){
            progressTimeViewUpdate(it)
        }

        viewModel.prepare(item.previewUrl)
    }


    private fun startPlayer() {
        playButton.setImageResource(R.drawable.pause_button)
    }

    private fun pausePlayer() {
        playButton.setImageResource(R.drawable.play_button)
    }

    private fun progressTimeViewUpdate(progressTime: String) {
        progressTimeView.text = progressTime
    }


    override fun onPause() {
        super.onPause()
        viewModel.playbackControl()
    }

    private fun render(state: PlayerState){
        when(state){
            PlayerState.PLAYING -> startPlayer()
            PlayerState.PAUSED, PlayerState.PREPARED -> pausePlayer()
        }
    }

}
