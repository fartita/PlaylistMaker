package com.example.playlistmaker.presentation.compose

import androidx.compose.runtime.Composable
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.widget.EditText
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.TopAppBar
import com.example.playlistmaker.presentation.viewmodels.search.SearchViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.data.states.FavouriteState
import com.example.playlistmaker.data.states.PlaylistState
import com.example.playlistmaker.data.states.SearchState
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.App.Companion.appColorScheme
import com.example.playlistmaker.presentation.viewmodels.library.PlayListLibraryViewModel
import com.example.playlistmaker.presentation.viewmodels.library.TracksViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediatekScreen(tracksViewModel: TracksViewModel,
                   playListsViewModel: PlayListLibraryViewModel,
                   onTrackClick: (Track) -> Unit,
                   onPlaylistClick: (Playlist) -> Unit,
                   onNewPlaylistClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 }, initialPage = 0)
    val selectedTabIndex = remember { mutableIntStateOf(pagerState.currentPage) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = appColorScheme.background)
            //.padding(top = 16.dp)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.mediatek),
                    color = appColorScheme.onBackground,
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.ys_display_medium, FontWeight.Bold))
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(titleContentColor = appColorScheme.background, containerColor = appColorScheme.background),
            modifier = Modifier.fillMaxWidth()
        )
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = selectedTabIndex.intValue,
            containerColor = appColorScheme.background,
            contentColor = appColorScheme.secondary,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    height = 2.dp,
                    color = Blue
                )
            },
            divider = {
                HorizontalDivider(
                    thickness = 0.dp,
                    color = Color.Transparent
                )
            }
        ) {
            Tab(
                selected = pagerState.currentPage == 0,
                selectedContentColor = appColorScheme.onBackground,
                unselectedContentColor = appColorScheme.onBackground,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                text = { Text(text = stringResource(id = R.string.favourites)) }
            )

            Tab(
                selected = pagerState.currentPage == 1,
                selectedContentColor = appColorScheme.onBackground,
                unselectedContentColor = appColorScheme.onBackground,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                text = { Text(text = stringResource(id = R.string.playlists)) },
            )
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(8.dp))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            when (page) {
                0 -> FavoriteTracksPage(
                    tracksViewModel = tracksViewModel,
                    onTrackClick = onTrackClick,
                )

                1 -> PlaylistsPage(
                    viewModel = playListsViewModel,
                    onPlaylistClick = onPlaylistClick,
                    onNewPlaylistClick = onNewPlaylistClick,
                )
            }
        }
    }
}

@Composable
fun FavoriteTracksPage(
    tracksViewModel: TracksViewModel,
    onTrackClick: (Track) -> Unit,
) {
    val state by tracksViewModel.stateFavouriteLiveData.observeAsState(FavouriteState.Empty)
    tracksViewModel.fill()
    when (state) {
        is FavouriteState.Content -> {
            val tracks = (state as FavouriteState.Content).tracks
            TrackItemList(
                Modifier.fillMaxSize(),
                tracks,
                onTrackClick
            )
        }

        is FavouriteState.Empty -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(id = R.string.tracks_message))
            }
        }
    }
}


@Composable
fun PlaylistsPage(
    viewModel: PlayListLibraryViewModel,
    onPlaylistClick: (Playlist) -> Unit,
    onNewPlaylistClick: () -> Unit,
) {
    val state by viewModel.stateLiveData.observeAsState(PlaylistState.Empty)
    viewModel.fill()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        AddPlayListButton(
            onNewPlaylistClick = onNewPlaylistClick
        )
        when (state) {
            is PlaylistState.Content -> {
                val playlists = (state as PlaylistState.Content).items
                PlaylistsGrid(playlists = playlists, onPlaylistClick = onPlaylistClick)
            }

            PlaylistState.Empty -> {
                EmptyPlayLists()
            }
        }
    }
}

@Composable
fun PlaylistsGrid(
    playlists: List<Playlist>,
    onPlaylistClick: (Playlist) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(playlists.size) { index ->
            PlaylistItem(
                playlist = playlists[index],
                onClick = { onPlaylistClick(playlists[index]) }
            )
        }
    }
}

@Composable
fun PlaylistItem(
    playlist: Playlist,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    val placeholder = painterResource(id = R.drawable.ic_track)

    DisposableEffect(playlist.imageUrl) {
        val job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val futureBitmap = playlist.imageUrl.let { uri ->
                    Glide.with(context)
                        .asBitmap()
                        .load(uri)
                        .submit()
                        .get()
                }
                withContext(Dispatchers.Main) {
                    bitmap = futureBitmap
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    bitmap = null
                }
            }
        }
        onDispose { job.cancel() }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(4.dp)
            .background(appColorScheme.background)
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxWidth()
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(appColorScheme.background)
                )
            } else {
                Image(
                    painter = placeholder,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                        .background(appColorScheme.background)
                )
            }
        }

        Text(
            text = playlist.name,
            color = appColorScheme.onBackground,
            fontSize = 16.sp,
            maxLines = 1,
            modifier = Modifier
                .padding(top = 4.dp)
        )

        Text(
            text = LocalContext.current.resources.getQuantityString(
                R.plurals.tracksContOfList, playlist.tracks.size, playlist.tracks.size
            ),
            color = appColorScheme.onBackground,
            fontSize = 16.sp,
            maxLines = 1
        )
    }
}

@Composable
fun EmptyPlayLists(
) {

}

@Composable
fun AddPlayListButton(
    onNewPlaylistClick: () -> Unit
) {
    val backgroundColor = appColorScheme.onBackground
    val textColor = appColorScheme.background
    val fontFamily = FontFamily(Font(R.font.ys_display_medium))

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onNewPlaylistClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor,
                contentColor = textColor
            ),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .wrapContentWidth()
                .height(56.dp)
                .wrapContentHeight()
                .padding(top = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.new_list),
                fontFamily = fontFamily,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}