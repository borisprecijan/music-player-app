package com.example.musicplayer.ui.library

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.musicplayer.MusicPlayerLibraryItem
import com.example.musicplayer.Route
import com.example.musicplayer.data.Music
import com.example.musicplayer.ui.MusicPlayerViewModel

@Composable
fun LibraryScreen(innerPadding: PaddingValues, musicPlayerViewModel: MusicPlayerViewModel, navController: NavController) {
    musicPlayerViewModel.setCurrentRouteTo("library")
    LibraryScreen(
        innerPadding = innerPadding,
        musicList = musicPlayerViewModel.uiState.musicList,
        isLoading = musicPlayerViewModel.uiState.isLoading,
        onLoadMusic = {
            musicPlayerViewModel.loadMusicFromDevice()
        },
        onMusicClick = {
            musicPlayerViewModel.onMusicClick(it)
            navController.navigate(Route.NowPlaying.name)
        }
    )
}

@Composable
private fun LibraryScreen(
    innerPadding: PaddingValues,
    musicList: List<Music>,
    isLoading: Boolean,
    onLoadMusic: () -> Unit,
    onMusicClick: (Music) -> Unit
) {
    val context = LocalContext.current as Activity

    BackHandler {
        context.moveTaskToBack(true)
    }

    LaunchedEffect(Unit) {
        onLoadMusic()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                if (musicList.isNotEmpty()) PaddingValues(0.dp)
                else innerPadding
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp)
            )
        } else {
            if (musicList.isEmpty()) {
                Text(text = "Nemate muzike na uređaju.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding
                ) {
                    items(
                        items = musicList,
                        key = { music ->
                            music.id
                        }
                    ) { music ->
                        MusicPlayerLibraryItem(
                            music = music,
                            onMusicClick = onMusicClick
                        )
                    }
                }
            }
        }
    }
}