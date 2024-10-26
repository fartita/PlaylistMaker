package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.model.Track
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity: AppCompatActivity()  {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY_MILLIS = 25L
        private const val TIME_FORMAT = "mm:ss"
        private const val ZERO_TIME = "00:00"

        fun startActivity(context: Context) {
            val intent = Intent(context, PlayerActivity::class.java)
            context.startActivity(intent)
        }
    }

    private var playerState = STATE_DEFAULT
    private lateinit var playButton: ImageButton
    private lateinit var progressTimeView: TextView
    private var mediaPlayer = MediaPlayer()
    private var mainThreadHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        val searchHistory = SearchHistory(this)


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

        mainThreadHandler = Handler(Looper.getMainLooper())
        val item = searchHistory.read()[0]

        preparePlayer(item)

        playButton.setOnClickListener {
            playbackControl()
        }

        val imageBack = findViewById<ImageView>(R.id.backButton)
        imageBack.setOnClickListener { finish() }



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
    }

    private fun preparePlayer(item: Track) {
        mediaPlayer.setDataSource(item.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED

        }
    }


    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.pause_button)
        playerState = STATE_PLAYING
        mainThreadHandler?.post(
            createUpdateProgressTime()
        )
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.play_button)
        playerState = STATE_PAUSED
    }

    private fun createUpdateProgressTime(): Runnable {
        return object : Runnable {
            override fun run() {
                when (playerState) {
                    STATE_PLAYING -> {
                        progressTimeView.text = SimpleDateFormat(
                            TIME_FORMAT,
                            Locale.getDefault()
                        ).format(mediaPlayer.currentPosition)
                        mainThreadHandler?.postDelayed(this, DELAY_MILLIS)
                    }
                    STATE_PAUSED -> {
                        mainThreadHandler?.removeCallbacks(this)
                    }
                    STATE_PREPARED -> {
                        mainThreadHandler?.removeCallbacks(this)
                        playButton.setImageResource(R.drawable.play_button)
                        progressTimeView.text = ZERO_TIME
                    }

                }
            }
        }

    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }


    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

}
