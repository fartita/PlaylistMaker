package com.example.playlistmaker.presentation.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Track
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Locale

class TracksInPlaylistAdapter(private val data: List<Track>,
                              private val deleteAlertDialog: MaterialAlertDialogBuilder,
                              private val clickListener: (Track) -> Unit,
                              private val longClickListener: (Track) -> Unit):
    RecyclerView.Adapter<TracksInPlaylistAdapter.TracksViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        return TracksViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_track_recycler, parent, false))
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            clickListener.invoke(data[position])
        }
        holder.itemView.setOnLongClickListener {
            deleteAlertDialog
                .setNegativeButton(R.string.negative_button) { _, _ ->
                }
                .setPositiveButton(R.string.yes_button) { _, _ ->
                    longClickListener.invoke(data[position])
                }
            deleteAlertDialog.show()
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class TracksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val trackName: TextView = itemView.findViewById(R.id.trackName)
        private val artistName: TextView = itemView.findViewById(R.id.trackInfo)
        private val avatar: ImageView = itemView.findViewById(R.id.imageTrack)


        fun bind(track: Track) {
            trackName.text = track.trackName
            val time =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime).toString()
            artistName.text = "${track.artistName} • ${time}"

            Glide.with(itemView)
                .load(track.artworkUrl100)
                .centerCrop()
                .placeholder(R.drawable.ic_track)
                .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_art)))
                .into(avatar)

        }
    }

}