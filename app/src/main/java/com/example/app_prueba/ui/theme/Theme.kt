// ruta: ui/theme/Theme.kt
package com.example.app_prueba.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Electric_Blue,
    secondary = Neon_Green,
    background = Black_Gamer,
    surface = Black_Gamer,
    onPrimary = White_Gamer,
    onSecondary = Black_Gamer,
    onBackground = White_Gamer,
    onSurface = White_Gamer,
    tertiary = Light_Gray_Gamer
)

@Composable
fun LevelUpGamerTheme(
    darkTheme: Boolean = true, // Forzamos el tema oscuro por defecto
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}