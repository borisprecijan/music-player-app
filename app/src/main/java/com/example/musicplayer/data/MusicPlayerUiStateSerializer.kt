package com.example.musicplayer.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.musicplayer.ui.MusicPlayerUiState
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

object MusicPlayerUiStateSerializer: Serializer<MusicPlayerUiState> {
    private val gson = Gson()

    override val defaultValue: MusicPlayerUiState
        get() = MusicPlayerUiState()

    override suspend fun readFrom(input: InputStream): MusicPlayerUiState {
        return try {
            val json = input.readBytes().decodeToString()
            gson.fromJson(json, MusicPlayerUiState::class.java)
        } catch (e: Exception) {
            throw CorruptionException("Cannot read data", e)
        }
    }

    override suspend fun writeTo(t: MusicPlayerUiState, output: OutputStream) {
        val json = gson.toJson(t)
        withContext(Dispatchers.IO) {
            output.write(json.toByteArray())
        }
    }
}