package com.example.musicplayer.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.example.musicplayer.data.MusicPlayerUiStateSerializer
import com.example.musicplayer.data.MusicRepository
import com.example.musicplayer.domain.GetMusicUseCase
import com.example.musicplayer.ui.MusicPlayerUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMusicRepository(@ApplicationContext context: Context): MusicRepository {
        return MusicRepository(context)
    }

    @Provides
    @Singleton
    fun provideGetMusicUseCase(musicRepository: MusicRepository): GetMusicUseCase {
        return GetMusicUseCase(musicRepository)
    }

    @Provides
    @Singleton
    fun provideMusicPlayerUiStateDataStore(@ApplicationContext context: Context): DataStore<MusicPlayerUiState> {
        return DataStoreFactory.create(
            serializer = MusicPlayerUiStateSerializer,
            produceFile = {
                context.dataStoreFile("ui_state.json")
            }
        )
    }

}