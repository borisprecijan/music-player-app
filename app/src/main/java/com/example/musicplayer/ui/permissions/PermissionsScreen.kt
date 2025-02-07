package com.example.musicplayer.ui.permissions

import android.Manifest
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.musicplayer.Route
import com.example.musicplayer.ui.MusicPlayerViewModel
import com.example.musicplayer.ui.theme.MusicPlayerTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionsScreen(
    onAllowClick: (Boolean) -> Unit = {}
) {
    val permissionState = rememberPermissionState(
        permission = Manifest.permission.READ_EXTERNAL_STORAGE,
        onPermissionResult = {
            onAllowClick(it)
        }
    )
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val boxMaxWidth = maxWidth
        val boxMaxHeight = maxHeight
        val portrait = boxMaxWidth < boxMaxHeight
        Column(
            modifier = if (portrait) {
                Modifier
                    .fillMaxWidth(0.8f)
                    .border(2.dp, Color.Black)
                    .padding(8.dp)
            } else {
                Modifier.fillMaxWidth(0.5f)
            },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Dobrodošli u aplikaciju Muzički plejer. Da biste nastavili, potrebno je da imate dozvolu za pristup skladištu uređaja kako biste učitali sve pesme sa Vašeg uređaja. Dozvolu možete tražiti klikom na dugme \"Dozvoli\".",
                textAlign = TextAlign.Justify
            )
            Button(
                onClick = {
                    if (!permissionState.status.isGranted)
                        permissionState.launchPermissionRequest()
                }
            ) {
                Text(text = "Dozvoli")
            }
        }
    }
}

@Composable
fun PermissionsScreen(
    innerPadding: PaddingValues,
    musicPlayerViewModel: MusicPlayerViewModel,
    navController: NavController
) {
    musicPlayerViewModel.setCurrentRouteTo(Route.Permissions)
    PermissionsScreen(
        onAllowClick = {
            if (it) {
                navController.navigate(Route.Library.name) {
                    popUpTo(Route.Splash.name) {
                        inclusive = true
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun PermissionsScreenPreview() {
    MusicPlayerTheme {
        PermissionsScreen()
    }
}