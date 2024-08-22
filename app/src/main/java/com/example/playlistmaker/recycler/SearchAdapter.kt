package com.example.playlistmaker.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class SearchAdapter(private val items: ArrayList<Track>) : RecyclerView.Adapter<SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_search_layout, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

class SearchViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.trackInfo)
    private val avatar: ImageView = itemView.findViewById(R.id.imageTrack)


    fun bind(track: Track) {
        trackName.text = track.trackName
        val time = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime).toString()
        artistName.text = "${track.artistName} • ${time}"

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.ic_track)
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_art)))
            .into(avatar)

    }
}
