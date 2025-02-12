package com.example.playlistmaker.presentation.recycler

import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemPlaylistBinding
import com.example.playlistmaker.domain.model.Playlist
import java.io.File

class PlaylistAdapter(
    val data: List<Playlist>,
    private val clickListener: (Playlist) -> Unit
) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):PlaylistViewHolder {
        val filePath =
            File(parent.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "album")
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(
            ItemPlaylistBinding.inflate(layoutInspector, parent, false),
            filePath
        )
    }
    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            clickListener.invoke(data[position])
        }
    }
    override fun getItemCount(): Int {
        return data.size
    }

    class PlaylistViewHolder(
        private val binding: ItemPlaylistBinding,
        private val filePath: File
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Playlist) {
            binding.plName.text = item.name
            val addedText = ContextCompat.getString(itemView.context, addText(item.trackCount))
            binding.plCount.text = "${item.trackCount} ${addedText}"
            val file = File(filePath, "${item.id}.jpg")
            Glide.with(itemView)
                .load(file.toUri().toString())
                .placeholder(R.drawable.ic_track)
                .transform(CenterCrop(), RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_art)))
                .into(binding.plImage)
        }
        private fun addText(trackCount: Long): Int {
            val dec = trackCount.toInt() % 100
            val ones = dec % 10
            val tens = dec / 10
            return if (ones == 1 && tens != 1) {
                R.string.one_track
            } else if (ones in 2..4 && tens != 1) {
                R.string.two_tracks
            } else {
                R.string.tracks
            }
        }
    }
}