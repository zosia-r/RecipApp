package com.example.recipapp.ui.screens

import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import com.example.recipapp.R
import kotlinx.coroutines.delay
import androidx.core.net.toUri

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val context = LocalContext.current
    // Pobranie adresu URI pliku wideo
    val videoUri = "android.resource://${context.packageName}/${R.raw.intro_video}".toUri()
    // coil + videoframe / media3exoplayer, fullscreen:
    // zmienić wygląd plusa - floating albo na srodku troche inaczej wygladajacy
    // Stan kontrolujący animację napisu
    var isVisible by remember { mutableStateOf(false) }

    // Uruchomienie animacji zaraz po załadowaniu ekranu
    LaunchedEffect(Unit) {
        isVisible = true
    }

    // Kompozycja ekranu startowego
    Box(modifier = Modifier.fillMaxSize()) {

        // 1. warstwa filmikowa - najniżej
        AndroidView(
            factory = { ctx ->
                VideoView(ctx).apply {
                    setVideoURI(videoUri)
                    // Wymuszenie zajęcia całego kontenera (MATCH_PARENT)
                    layoutParams = android.view.ViewGroup.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setOnPreparedListener { mediaPlayer ->
                        mediaPlayer.isLooping = true
                        mediaPlayer.start()
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // 2. warstwa tekstowa (animowany napis)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 2500))
            ) {
                Text(
                    text = "RecipApp",
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    // 3. przełącz się na ekran główny
    LaunchedEffect(Unit) {
        delay(5000)
        onTimeout()
    }
}