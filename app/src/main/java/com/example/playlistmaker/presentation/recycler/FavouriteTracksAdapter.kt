package com.example.playlistmaker.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemTrackRecyclerBinding
import com.example.playlistmaker.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class FavouriteTracksAdapter(private val data: List<Track>, private val clickListener: (Track) -> Unit) :
    RecyclerView.Adapter<FavouriteTracksAdapter.FavouriteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return FavouriteViewHolder(ItemTrackRecyclerBinding.inflate(layoutInspector, parent, false))
    }
    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            clickListener.invoke(data[position])
        }
    }
    override fun getItemCount(): Int {
        return data.size
    }

    class FavouriteViewHolder(private val binding: ItemTrackRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Track) {
            binding.trackName.text = item.trackName
            val time =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTime)
            binding.trackInfo.text = "${item.artistName} • ${time}"
            Glide.with(itemView)
                .load(item.artworkUrl100)
                .placeholder(R.drawable.ic_track)
                .centerCrop()
                .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_art)))
                .into(binding.imageTrack)
        }
    }
}