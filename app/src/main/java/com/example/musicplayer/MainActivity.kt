package com.example.musicplayer

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicplayer.ui.MusicPlayerViewModel
import com.example.musicplayer.ui.library.LibraryScreen
import com.example.musicplayer.ui.nowplaying.NowPlayingScreen
import com.example.musicplayer.ui.splash.SplashScreen
import com.example.musicplayer.ui.theme.MusicPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerAppContent()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MusicPlayerAppContent(musicPlayerViewModel: MusicPlayerViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    MusicPlayerTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            topBar = {
                when(musicPlayerViewModel.uiState.currentRoute) {
                    Route.Library -> {
                        MusicPlayerTopBar(title = stringResource(R.string.library_title))
                    }
                    Route.NowPlaying -> {
                        MusicPlayerTopBar(title = stringResource(R.string.now_playing_title))
                    }
                    else -> {}
                }
            }, bottomBar = {
                if (musicPlayerViewModel.uiState.currentMusic != null && musicPlayerViewModel.uiState.currentRoute == Route.Library) {
                    MusicPlayerBottomBar(musicPlayerViewModel = musicPlayerViewModel, navController = navController)
                }
            }
        ) { innerPadding ->
            MusicPlayerNavigation(navController, musicPlayerViewModel, innerPadding)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MusicPlayerNavigation(
    navController: NavHostController,
    musicPlayerViewModel: MusicPlayerViewModel,
    innerPadding: PaddingValues
) {
    NavHost(navController = navController, startDestination = Route.Splash.name) {
        composable(route = Route.Splash.name) {
            SplashScreen(
                innerPadding = innerPadding,
                musicPlayerViewModel = musicPlayerViewModel,
                navController = navController
            )
        }
        composable(route = Route.Library.name) {
            LibraryScreen(
                innerPadding = innerPadding,
                musicPlayerViewModel = musicPlayerViewModel,
                navController = navController
            )
        }
        composable(route = Route.NowPlaying.name) {
            NowPlayingScreen(
                innerPadding = innerPadding,
                musicPlayerViewModel = musicPlayerViewModel
            )
        }
    }
}