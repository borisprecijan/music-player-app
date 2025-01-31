package com.example.musicplayer

sealed class Route(val name: String) {
    data object Splash: Route("splash")
    data object Library: Route("library")
    data object NowPlaying: Route("nowPlaying")
    data object Permissions: Route("permissions")
}