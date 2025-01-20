package com.example.musicplayer.data

import android.annotation.SuppressLint

data class Music(
    val id: Int,
    val name: String,
    val currentProgress: Long,
    val duration: Long,
    val path: String
)

@SuppressLint("DefaultLocale")
fun Music.getDurationAsString(): String {
    val duration = this.duration
    val minutes = duration / 1000 / 60
    val seconds = (duration / 1000) % 60
    return String.format("%02d:%02d", minutes, seconds)
}

@SuppressLint("DefaultLocale")
fun Music.getCurrentProgressAsString(): String {
    val currentProgress = this.currentProgress
    val minutes = currentProgress / 1000 / 60
    val seconds = (currentProgress / 1000) % 60
    return String.format("%02d:%02d", minutes, seconds)
}

fun Music.getProgressBarValue(): Float {
    val currentProgress = this.currentProgress.toFloat()
    val duration = this.duration.toFloat()
    return currentProgress / duration
}
