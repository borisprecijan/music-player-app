package com.example.musicplayer.data

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.musicplayer.R

class MusicService : Service() {
    private var musicPlayer: MediaPlayer = MediaPlayer()
    private val binder: MusicBinder = MusicBinder()

    inner class MusicBinder: Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onDestroy() {
        super.onDestroy()
        musicPlayer.release()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    fun showNotification(music: Music) {
        val notification = createNotification(music)
        startForeground(1, notification)
    }

    private fun createNotification(music: Music): Notification {
        val channelId = "channelId"

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.music_note_icon)
            .setContentTitle("Obaveštenje - Sada se sluša")
            .setContentText(music.name)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
        return notification
    }

    fun getCurrentPosition(): Int {
        return musicPlayer.currentPosition
    }

    fun play() {
        musicPlayer.start()
    }

    fun isPlaying(): Boolean = musicPlayer.isPlaying

    fun prepare(music: Music) {
        musicPlayer.reset()
        musicPlayer.setDataSource(music.path)
        musicPlayer.prepare()
    }

    fun setOnCompletionListener(onComplete: () -> Unit) {
        musicPlayer.setOnCompletionListener {
            onComplete()
        }
    }

    fun pause() {
        musicPlayer.pause()
    }

    fun seekTo(position: Int) {
        musicPlayer.seekTo(position)
    }
}