package com.example.musicplayer.data

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class MusicPlayerService : Service() {
    private val musicPlayer = MediaPlayer()
    private val binder: MusicBinder = MusicBinder()

    inner class MusicBinder: Binder() {
        fun getService(): MusicPlayerService {
            return this@MusicPlayerService
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
}