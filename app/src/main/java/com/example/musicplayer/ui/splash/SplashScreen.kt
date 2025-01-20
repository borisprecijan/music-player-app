package com.example.musicplayer.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.musicplayer.R
import com.example.musicplayer.Route
import com.example.musicplayer.ui.MusicPlayerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(innerPadding: PaddingValues, musicPlayerViewModel: MusicPlayerViewModel, navController: NavController) {
    musicPlayerViewModel.setCurrentRouteTo(Route.Splash)
    SplashScreen(
        innerPadding = innerPadding,
        onFinish = {
            navController.navigate(Route.Library.name) {
                popUpTo(Route.Splash.name) {
                    inclusive = true
                }
            }
        }
    )
}

@Composable
private fun SplashScreen(innerPadding: PaddingValues, onFinish: () -> Unit) {
    LaunchedEffect(Unit) {
        this.launch {
            delay(3000)
            onFinish()
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        val boxMaxWidth = maxWidth
        val boxMaxHeight = maxHeight
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = if (boxMaxWidth < boxMaxHeight) {
                    Modifier
                        .fillMaxWidth(0.3f)
                        .aspectRatio(1f)
                } else {
                    Modifier
                        .fillMaxHeight(0.3f)
                        .aspectRatio(1f, matchHeightConstraintsFirst = true)
                },
                imageVector = Icons.Default.MusicNote,
                contentDescription = stringResource(R.string.splash_icon_desc),
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
