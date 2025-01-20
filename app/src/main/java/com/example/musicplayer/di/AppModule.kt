package com.example.musicplayer.di

import android.content.Context
import com.example.musicplayer.data.MusicRepository
import com.example.musicplayer.domain.GetMusicUseCase
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

}