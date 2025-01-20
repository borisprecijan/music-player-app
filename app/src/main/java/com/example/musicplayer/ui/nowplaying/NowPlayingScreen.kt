package com.example.musicplayer.ui.nowplaying

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.musicplayer.R
import com.example.musicplayer.Route
import com.example.musicplayer.data.Music
import com.example.musicplayer.data.getCurrentProgressAsString
import com.example.musicplayer.data.getDurationAsString
import com.example.musicplayer.data.getProgressBarValue
import com.example.musicplayer.ui.MusicPlayerViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NowPlayingScreen(innerPadding: PaddingValues, musicPlayerViewModel: MusicPlayerViewModel) {
    musicPlayerViewModel.setCurrentRouteTo(Route.NowPlaying)
    NowPlayingScreen(
        innerPadding = innerPadding,
        music = musicPlayerViewModel.uiState.currentMusic!!,
        isPlaying = musicPlayerViewModel.uiState.isPlaying,
        onCurrentProgressChange = {
            musicPlayerViewModel.seekTo(it)
        },
        onPreviousClick = {
            musicPlayerViewModel.onPreviousClick()
        },
        onPlayOrPauseClick = {
            musicPlayerViewModel.onPlayOrPause()
        },
        onNextClick = {
            musicPlayerViewModel.onNextClick()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NowPlayingScreen(
    innerPadding: PaddingValues,
    music: Music,
    isPlaying: Boolean,
    onCurrentProgressChange: (Float) -> Unit,
    onPreviousClick: () -> Unit,
    onPlayOrPauseClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(innerPadding)
    ) {
        Icon(
            imageVector = Icons.Default.MusicNote,
            contentDescription = stringResource(R.string.now_playing_music_note_icon_desc),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.6f)
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = music.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Slider(
            modifier = Modifier.fillMaxHeight(.05f),
            value = music.getProgressBarValue(),
            onValueChange = { newValue ->
                onCurrentProgressChange(newValue)
            },
            /*onValueChangeFinished = {
                onUpdate()
            },*/
            valueRange = 0f..1f,
            thumb = {
                Icon(
                    modifier = Modifier.scale(0.7f).border(2.dp, Color.Red, shape = CircleShape),
                    imageVector = Icons.Filled.Circle,
                    contentDescription = stringResource(R.string.now_playing_music_slider_thumb_icon_desc),
                    tint = Color.White
                )
            },
            track = {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = Color.Gray,
                    content = {}
                )
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = music.getCurrentProgressAsString())
            Text(text = music.getDurationAsString())
        }
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedIconButton (
                modifier = Modifier.size(64.dp),
                onClick = {
                    onPreviousClick()
                },
                colors = IconButtonDefaults.outlinedIconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Default.SkipPrevious,
                    contentDescription = stringResource(R.string.now_playing_previous_music_icon_desc)
                )
            }
            OutlinedIconButton (
                modifier = Modifier.size(64.dp),
                onClick = {
                    onPlayOrPauseClick()
                },
                colors = IconButtonDefaults.outlinedIconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = if (!isPlaying) Icons.Default.PlayArrow else Icons.Default.Pause,
                    contentDescription = "Play Or Pause Music Icon"
                )
            }
            OutlinedIconButton (
                modifier = Modifier.size(64.dp),
                onClick = {
                    onNextClick()
                },
                colors = IconButtonDefaults.outlinedIconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "Next Music Icon"
                )
            }
        }
    }
}