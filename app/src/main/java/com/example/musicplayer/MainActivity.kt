package com.example.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicplayer.ui.MusicPlayerViewModel
import com.example.musicplayer.ui.library.LibraryScreen
import com.example.musicplayer.ui.nowplaying.NowPlayingScreen
import com.example.musicplayer.ui.permissions.PermissionsScreen
import com.example.musicplayer.ui.splash.SplashScreen
import com.example.musicplayer.ui.theme.MusicPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MusicPlayerAppContent()
        }
    }
}

@Composable
fun MusicPlayerAppContent(musicPlayerViewModel: MusicPlayerViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val context = LocalContext.current

    DisposableEffect(Unit) {
        if (!musicPlayerViewModel.started) {
            musicPlayerViewModel.startService(context)
            musicPlayerViewModel.started = true
        }
        musicPlayerViewModel.bindService(context)

        onDispose {
            musicPlayerViewModel.unbindService(context)
        }
    }

    MusicPlayerTheme(
        darkTheme = false
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            topBar = {
                MusicPlayerTopBar(musicPlayerViewModel = musicPlayerViewModel)
            }, bottomBar = {
                MusicPlayerBottomBar(musicPlayerViewModel = musicPlayerViewModel, navController = navController)
            }
        ) { innerPadding ->
            MusicPlayerNavigation(
                navController = navController,
                musicPlayerViewModel = musicPlayerViewModel,
                innerPadding = innerPadding
            )
        }
    }
}

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
        composable(route = Route.Permissions.name) {
            PermissionsScreen(
                innerPadding = innerPadding,
                musicPlayerViewModel = musicPlayerViewModel,
                navController = navController
            )
        }
    }
}