package com.example.musicplayer.domain

import com.example.musicplayer.data.Music
import com.example.musicplayer.data.MusicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMusicUseCase @Inject constructor(private val musicRepository: MusicRepository) {
    suspend operator fun invoke(): List<Music> {
        var musicList: MutableList<Music>
        withContext(Dispatchers.IO) {
            musicList = musicRepository.getMusicFromDevice().toMutableList()
        }
        return musicList
    }
}