package com.example.playlistmaker.presentation.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.data.states.SearchState
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.App.Companion.appColorScheme
import com.example.playlistmaker.presentation.viewmodels.search.SearchViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel, onTrackClickDebounce: (Track) -> Unit, modifier: Modifier = Modifier,) {
    val currentData by viewModel.stateData.observeAsState()
    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.background(color = appColorScheme.background)) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.search),
                    color = appColorScheme.onBackground,
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.ys_display_medium, FontWeight.Bold))
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(titleContentColor = appColorScheme.background, containerColor = appColorScheme.background),
            modifier = Modifier.fillMaxWidth()
        )
        SearchTextField(text = text,
            hint = stringResource(R.string.search),
            onTextChange = { inputText ->
                text = inputText
            },
            onFocusEvent = {
                viewModel.onTextChanged(text)
                viewModel.searchDebounce()
            })
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
        when (currentData) {
            is SearchState.Content -> TrackItemList(
                Modifier.fillMaxSize(),
                (currentData as SearchState.Content).tracks,
                onTrackClickDebounce
            )
            is SearchState.Empty -> ErrorMessage(messageText = (currentData as SearchState.Empty).message, onButtonClick = {viewModel.repeatSearch()})
            is SearchState.Loading -> LoadingView()
            is SearchState.EmptyInput -> HistoryScreen(modifier = modifier,
                trackList = (currentData as SearchState.EmptyInput).tracks,
                clickListener = onTrackClickDebounce,
                onButtonClick = { viewModel.clear() })
            else -> Unit
        }
    }
}

@Composable
fun HistoryScreen(
    modifier: Modifier,
    trackList: List<Track>,
    clickListener: (Track) -> Unit,
    onButtonClick: () -> Unit
) {
    if (trackList.isNotEmpty()) {
        Column() {
            Text(
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                color = appColorScheme.onBackground,
                fontSize = 20.sp,
                text = stringResource(id = R.string.you_search)
            )
            TrackItemList(
                trackList = trackList, clickListener = clickListener
            )
            Button(
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(54.dp))
                    .padding(top = 24.dp),
                onClick = onButtonClick,
                colors = ButtonColors(
                    containerColor = appColorScheme.onBackground,
                    contentColor = appColorScheme.background,
                    disabledContentColor = Color.Unspecified,
                    disabledContainerColor = Color.Unspecified
                )
            ) {
                Text(
                    text = stringResource(id = R.string.clear_history),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = appColorScheme.background
                    )
                )
            }
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}



@Composable
fun ErrorMessage(
    modifier: Modifier = Modifier.fillMaxSize()
        .padding(top = 86.dp),
    messageText: Int,
    onButtonClick: () -> Unit
) {
    val drawable: Int = when(messageText){
        R.string.network_problem -> R.drawable.ic_no_internet
        R.string.nothing_found -> R.drawable.ic_nothing_found
        else -> R.drawable.ic_nothing_found
    }
    Column(modifier) {
        Image(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            painter = painterResource(id = drawable),
            contentDescription = null,
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(messageText),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = appColorScheme.secondary,
                fontSize = 20.sp
            )
        )
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 24.dp),
            onClick = onButtonClick,
            colors = ButtonColors(
                containerColor = appColorScheme.onBackground,
                contentColor = appColorScheme.background,
                disabledContentColor = Color.Unspecified,
                disabledContainerColor = Color.Unspecified
            )
        ) {
            Text(
                text = stringResource(id = R.string.refresh_button),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = appColorScheme.background,
                    fontSize = 14.sp
                )
            )
        }
    }
}

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    hint: String = "",
    onTextChange: (String) -> Unit,
    onFocusEvent: () -> Unit
) {
    Box {
        BasicTextField(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(appColorScheme.surfaceContainer)
                .onFocusEvent { onFocusEvent.invoke() },
            value = text,
            onValueChange = onTextChange,
            singleLine = true,
            cursorBrush = SolidColor(Blue),
            textStyle = MaterialTheme.typography.titleSmall.copy(
                color = YpBlack
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        tint = appColorScheme.onSurface
                    )
                    Box(
                        modifier = modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                text = hint,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = appColorScheme.onSecondary
                                ),
                            )
                        }
                        innerTextField()
                    }
                    if (text.isNotEmpty()) {
                        Icon(painter = painterResource(id = R.drawable.ic_clear),
                            contentDescription = "Clear Icon",
                            tint = appColorScheme.onSurface,
                            modifier = Modifier.clickable { onTextChange("") })
                    }
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search)
        )
    }
}
