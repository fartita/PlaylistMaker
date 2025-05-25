package com.example.playlistmaker.presentation.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.App.Companion.appColorScheme
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TrackItemList(modifier: Modifier = Modifier,trackList: List<Track>, clickListener: (Track) -> Unit) {
    Column(modifier.verticalScroll(rememberScrollState())) {
        for (track in trackList) {
            TrackCard(track, clickListener)
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TrackCard(track: Track, clickListener: (Track) -> Unit) {
    val time =  SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime).toString()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .clickable(onClick = { clickListener(track) })
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            GlideImage(
                modifier = Modifier
                    .clip(RoundedCornerShape(2.dp))
                    .size(45.dp),
                model = track.artworkUrl100,
                contentScale = ContentScale.Fit,
                loading = placeholder(R.drawable.ic_track),
                failure = placeholder(R.drawable.ic_track),
                contentDescription = track.trackName
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    text = track.trackName,
                    color = appColorScheme.onBackground
                )
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(id = R.color.grey),
                    text = "${track.artistName} -  $time"
                )
            }
            Icon(
                modifier = Modifier.align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = null,
                tint = appColorScheme.onBackground
            )

        }
    }
}