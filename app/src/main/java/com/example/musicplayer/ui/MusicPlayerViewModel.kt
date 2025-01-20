package com.example.musicplayer.ui

import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.Route
import com.example.musicplayer.data.Music
import com.example.musicplayer.domain.GetMusicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(private val getMusicUseCase: GetMusicUseCase): ViewModel() {
    private var _uiState by mutableStateOf(
        MusicPlayerUiState()
    )

    val uiState get() = _uiState

    fun loadMusicFromDevice() {
        viewModelScope.launch {
            _uiState = _uiState.copy(
                isLoading = true
            )
            val musicList = getMusicUseCase.invoke()
            delay(500)
            _uiState = _uiState.copy(
                musicList = musicList,
                isLoading = false
            )
        }
    }

    fun setCurrentRouteTo(newRoute: Route) {
        viewModelScope.launch {
            _uiState = _uiState.copy(
                currentRoute = newRoute
            )
        }
    }

    //prebaciti u servis
    private val musicPlayer: MediaPlayer = MediaPlayer()

    fun onMusicClick(music: Music) {
        viewModelScope.launch {
            _uiState = _uiState.copy(
                currentMusic = music
            )
            musicPlayer.reset()
            musicPlayer.setDataSource(_uiState.currentMusic!!.path)
            musicPlayer.prepare()
            musicPlayer.setOnCompletionListener {
                onNextClick()
            }
            musicPlayer.start()
            _uiState = _uiState.copy(
                isPlaying = true
            )
            updateCurrentMusicProgress()
        }
    }

    //jako neefikasno, ali radi
    private fun findIndexOf(music: Music): Int {
        _uiState.musicList.forEachIndexed { i, m ->
            if (music.id == m.id)
                return i
        }
        return -1
    }

    fun onNextClick() {
        viewModelScope.launch {
            var currentIndex = findIndexOf(_uiState.currentMusic!!)
            if (currentIndex == _uiState.musicList.size - 1)
                currentIndex = 0
            else
                currentIndex += 1
            val nextMusic = _uiState.musicList[currentIndex]
            _uiState = _uiState.copy(
                currentMusic = nextMusic
            )
            musicPlayer.reset()
            musicPlayer.setDataSource(_uiState.currentMusic!!.path)
            musicPlayer.prepare()
            musicPlayer.start()
            _uiState = _uiState.copy(
                isPlaying = true
            )
            updateCurrentMusicProgress()
        }
    }

    fun onPreviousClick() {
        viewModelScope.launch {
            var currentIndex = findIndexOf(_uiState.currentMusic!!)
            if (currentIndex == 0)
                currentIndex = _uiState.musicList.size - 1
            else
                currentIndex -= 1
            val previousMusic = _uiState.musicList[currentIndex]
            _uiState = _uiState.copy(
                currentMusic = previousMusic
            )
            musicPlayer.reset()
            musicPlayer.setDataSource(_uiState.currentMusic!!.path)
            musicPlayer.prepare()
            musicPlayer.start()
            _uiState = _uiState.copy(
                isPlaying = true
            )
            updateCurrentMusicProgress()
        }
    }

    fun onPlayOrPause() {
        viewModelScope.launch {
            if (_uiState.isPlaying) {
                musicPlayer.pause()
                _uiState = _uiState.copy(
                    isPlaying = false
                )
            } else {
                musicPlayer.start()
                _uiState = _uiState.copy(
                    isPlaying = true
                )
                updateCurrentMusicProgress()
            }
        }
    }

    fun seekTo(newValue: Float) {
        viewModelScope.launch {
            val seekPosition: Long = (newValue * musicPlayer.duration).toLong()

            musicPlayer.seekTo(seekPosition.toInt())
            _uiState = _uiState.copy(
                currentMusic = _uiState.currentMusic!!.copy(
                    currentProgress = seekPosition
                )
            )
        }
    }

    private suspend fun updateCurrentMusicProgress() {
        while (_uiState.isPlaying) {
            _uiState = _uiState.copy(
                currentMusic = _uiState.currentMusic!!.copy(
                    currentProgress = musicPlayer.currentPosition.toLong()
                )
            )
            delay(1000)
        }
    }
}