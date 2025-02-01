package com.example.musicplayer.data

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.musicplayer.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MusicService : Service() {
    private var musicPlayer: MediaPlayer = MediaPlayer()
    private val binder: MusicBinder = MusicBinder()
    private val serviceScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())//+SupervisorJob()????

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

    fun getCurrentPosition(): Int = musicPlayer.currentPosition

    fun play() {
        //serviceScope.launch {
            musicPlayer.start()
        //}
    }

    fun isPlaying(): Boolean = musicPlayer.isPlaying

    fun prepare(music: Music) {
        //serviceScope.launch {
            musicPlayer.reset()
            musicPlayer.setDataSource(music.path)
            musicPlayer.prepare()
        //}
    }

    fun setOnCompletionListener(onComplete: () -> Unit) {
        //serviceScope.launch {
            musicPlayer.setOnCompletionListener {
                onComplete()
            }
        //}
    }

    fun pause() {
        //serviceScope.launch {
            musicPlayer.pause()
        //}
    }

    fun seekTo(position: Int) {
        //serviceScope.launch {
            musicPlayer.seekTo(position)
        //}
    }
}