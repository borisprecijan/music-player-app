package com.example.musicplayer.ui

import com.example.musicplayer.Route
import com.example.musicplayer.data.Music
import com.google.gson.annotations.SerializedName

data class MusicPlayerUiState(
    @SerializedName("isPlaying") val isPlaying: Boolean = false,
    @SerializedName("currentMusic") val currentMusic: Music? = null,
    @SerializedName("musicList") val musicList: List<Music> = emptyList(),
    @SerializedName("isLoading") val isLoading: Boolean = false,
    @SerializedName("currentRoute") val currentRoute: Route = Route.Splash,
    @SerializedName("isPermissionGranted") val isPermissionGranted: Boolean = false,
    @SerializedName("isServiceBound") val isServiceBound: Boolean = false
)
