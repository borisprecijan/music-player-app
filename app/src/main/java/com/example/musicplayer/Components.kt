package com.example.musicplayer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.musicplayer.data.Music
import com.example.musicplayer.data.getProgressBarValue
import com.example.musicplayer.ui.MusicPlayerViewModel
import com.example.musicplayer.ui.theme.MusicPlayerTheme

@Composable
fun MusicPlayerTopBar(musicPlayerViewModel: MusicPlayerViewModel) {
    when(musicPlayerViewModel.uiState.currentRoute) {
        Route.Library -> {
            MusicPlayerTopBar(title = stringResource(R.string.library_title))
        }
        Route.NowPlaying -> {
            MusicPlayerTopBar(title = stringResource(R.string.now_playing_title))
        }
        else -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MusicPlayerTopBar(title: String) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = title)
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Preview
@Composable
private fun MusicPlayerTopBarPreview() {
    MusicPlayerTheme {
        MusicPlayerTopBar(title = "Library")
    }
}

@Composable
fun MusicPlayerBottomBar(musicPlayerViewModel: MusicPlayerViewModel, navController: NavController) {
    if (musicPlayerViewModel.uiState.currentMusic != null && musicPlayerViewModel.uiState.currentRoute == Route.Library) {
        MusicPlayerBottomBar(
            currentMusic = musicPlayerViewModel.uiState.currentMusic!!,
            isPlaying = musicPlayerViewModel.uiState.isPlaying,
            onClick = {
                navController.navigate(Route.NowPlaying.name)
                musicPlayerViewModel.setCurrentRouteTo(Route.NowPlaying)
            },
            onPreviousClick = {
                musicPlayerViewModel.onPreviousClick()
            },
            onPlayPauseClick = {
                musicPlayerViewModel.onPlayOrPause()
            },
            onNextClick = {
                musicPlayerViewModel.onNextClick()
            }
        )
    }
}

@Composable
private fun MusicPlayerBottomBar(
    currentMusic: Music?,
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onClick: () -> Unit
) {
    BottomAppBar(
        modifier = Modifier.clickable {
            onClick()
        },
        contentPadding = PaddingValues(all = 0.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Icon(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f),
            imageVector = Icons.Default.MusicNote,
            contentDescription = ""
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row {
                LinearProgressIndicator(
                    progress = {
                        currentMusic!!.getProgressBarValue()
                    },
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = currentMusic!!.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier
                        .padding(all = 4.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedIconButton (
                        modifier = Modifier.fillMaxHeight()
                            .aspectRatio(1f, matchHeightConstraintsFirst = true)
                            .padding(all = 2.dp),
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
                        modifier = Modifier.fillMaxHeight()
                            .aspectRatio(1f, matchHeightConstraintsFirst = true)
                            .padding(all = 2.dp),
                        onClick = {
                            onPlayPauseClick()
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
                        modifier = Modifier.fillMaxHeight()
                            .aspectRatio(1f, matchHeightConstraintsFirst = true)
                            .padding(all = 2.dp),
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
    }
}

@Preview
@Composable
private fun MusicPlayerBottomBarPreview() {
    MusicPlayerTheme {
        MusicPlayerBottomBar(
            currentMusic = Music(1, "Music Name", 76, 100, "Path"),
            isPlaying = false,
            onPlayPauseClick = {},
            onPreviousClick = {},
            onNextClick = {},
            onClick = {}
        )
    }
}

@Composable
fun MusicPlayerLibraryItem(
    modifier: Modifier = Modifier,
    music: Music,
    onMusicClick: (Music) -> Unit
) {
    ElevatedCard (
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        onClick = {
            onMusicClick(music)
        }
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.AudioFile,
                contentDescription = "Audio File Icon"
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                text = music.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
private fun MusicPlayerLibraryItemPreview() {
    MusicPlayerTheme {
        MusicPlayerLibraryItem(
            music = Music(1, "Music Name", 12, 123, "Path"),
            onMusicClick = {}
        )
    }
}