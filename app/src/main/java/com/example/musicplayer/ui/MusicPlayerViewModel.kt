package com.example.musicplayer.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.Music
import com.example.musicplayer.data.MusicService
import com.example.musicplayer.domain.GetMusicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(private val getMusicUseCase: GetMusicUseCase, private val musicPlayerUiStateDataSore: DataStore<MusicPlayerUiState>): ViewModel() {
    private var _uiState by mutableStateOf(
        MusicPlayerUiState()
    )

    val uiState get() = _uiState

    private var musicServiceBinder: MusicService.MusicBinder? = null

    init {
        viewModelScope.launch {
            val test = async(Dispatchers.IO) {
                musicPlayerUiStateDataSore.data.first()
            }
            _uiState = test.await()
            //_uiState = withContext(Dispatchers.IO) { musicPlayerUiStateDataSore.data.first() }
            withContext(Dispatchers.IO) {
                delay(1000)
                musicServiceBinder!!.getService().prepare(_uiState.currentMusic!!)
                musicServiceBinder!!.getService().setOnCompletionListener {
                    onNextClick()
                }
                musicServiceBinder!!.getService().seekTo(_uiState.currentMusic!!.currentProgress)
            }
        }
    }

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

    fun setCurrentRouteTo(newRoute: String) {
        viewModelScope.launch {
            _uiState = _uiState.copy(
                currentRoute = newRoute
            )
        }
    }

    fun onMusicClick(music: Music) {
        viewModelScope.launch {
            _uiState = _uiState.copy(
                currentMusic = music
            )
            musicServiceBinder!!.getService().prepare(_uiState.currentMusic!!)
            musicServiceBinder!!.getService().setOnCompletionListener {
                onNextClick()
            }
            musicServiceBinder!!.getService().play()
            musicServiceBinder!!.getService().showNotification(_uiState.currentMusic!!)
            _uiState = _uiState.copy(
                isPlaying = true
            )
            //saveState()
            updateCurrentMusicProgress()
        }
    }

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
            musicServiceBinder!!.getService().prepare(_uiState.currentMusic!!)
            musicServiceBinder!!.getService().setOnCompletionListener {
                onNextClick()
            }
            musicServiceBinder!!.getService().play()
            musicServiceBinder!!.getService().showNotification(_uiState.currentMusic!!)
            _uiState = _uiState.copy(
                isPlaying = true
            )
            //saveState()
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
            musicServiceBinder!!.getService().prepare(_uiState.currentMusic!!)
            musicServiceBinder!!.getService().setOnCompletionListener {
                onNextClick()
            }
            musicServiceBinder!!.getService().play()
            musicServiceBinder!!.getService().showNotification(_uiState.currentMusic!!)
            _uiState = _uiState.copy(
                isPlaying = true
            )
            //saveState()
            updateCurrentMusicProgress()
        }
    }

    fun onPlayOrPause() {
        viewModelScope.launch {
            //saveState()
            if (musicServiceBinder!!.getService().isPlaying()) {
                musicServiceBinder!!.getService().pause()
                _uiState = _uiState.copy(
                    isPlaying = false
                )
            } else {
                musicServiceBinder!!.getService().showNotification(_uiState.currentMusic!!)
                musicServiceBinder!!.getService().play()
                _uiState = _uiState.copy(
                    isPlaying = true
                )
                updateCurrentMusicProgress()
            }
        }
    }

    fun onSeekTo(newValue: Float) {
        viewModelScope.launch {
            val newPosition = (newValue * _uiState.currentMusic!!.duration).toInt()
            musicServiceBinder!!.getService().seekTo(newPosition)
            //saveState()
            _uiState = _uiState.copy(
                currentMusic = _uiState.currentMusic!!.copy(
                    currentProgress = musicServiceBinder!!.getService().getCurrentPosition()
                )
            )
        }
    }

    private suspend fun updateCurrentMusicProgress() {
        while (_uiState.isPlaying) {
            _uiState = _uiState.copy(
                currentMusic = _uiState.currentMusic!!.copy(
                    currentProgress = musicServiceBinder!!.getService().getCurrentPosition()
                )
            )
            saveState()
            delay(1000)
        }
    }

    private val serviceConnection: ServiceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            musicServiceBinder = service as MusicService.MusicBinder
            _uiState = _uiState.copy(
                isServiceBound = true
            )
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicServiceBinder = null
            _uiState = _uiState.copy(
                isServiceBound = false
            )
        }
    }

    fun bindService(context: Context) {
        viewModelScope.launch {
            if (!_uiState.isServiceBound) {
                val serviceIntent = Intent(context, MusicService::class.java)
                context.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
                _uiState = _uiState.copy(
                    isServiceBound = true
                )
            }
        }
    }

    fun unbindService(context: Context) {
        viewModelScope.launch {
            if (_uiState.isServiceBound) {
                context.unbindService(serviceConnection)
                _uiState = _uiState.copy(
                    isServiceBound = false
                )
            }
        }
    }

    private fun saveState() {
        viewModelScope.launch {
            musicPlayerUiStateDataSore.updateData {
                _uiState.copy(
                    isPlaying = false
                )
            }
        }
    }
}