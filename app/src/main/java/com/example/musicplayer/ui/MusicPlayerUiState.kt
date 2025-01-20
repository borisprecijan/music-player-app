package com.example.musicplayer.ui

import com.example.musicplayer.Route
import com.example.musicplayer.data.Music

data class MusicPlayerUiState(
    val isPlaying: Boolean = false,
    val currentMusic: Music? = null,
    val musicList: List<Music> = emptyList(),
    val isLoading: Boolean = false,
    val currentRoute: Route = Route.Splash
)
