package com.example.musicplayer

import com.google.gson.annotations.SerializedName

enum class Route {
    @SerializedName("splash") Splash,
    @SerializedName("library") Library,
    @SerializedName("nowPlaying") NowPlaying,
    @SerializedName("permissions") Permissions
}