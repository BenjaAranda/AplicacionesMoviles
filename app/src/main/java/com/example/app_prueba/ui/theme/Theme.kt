package com.example.app_prueba.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color // <-- IMPORT AÑADIDO
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = LevelUpPurple,
    secondary = LevelUpGreen,
    background = DarkBackground,
    surface = SurfaceColor,
    surfaceVariant = SurfaceColor,
    onPrimary = TextPrimary,
    onSecondary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    tertiary = TextSecondary,
    error = Color(0xFFE94560)
)

@Composable
fun LevelUpGamerTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

// 1. Define tus colores personalizados
val CustomGreen = Color(0xFF38D717) // Un verde neón
val CustomPurple = Color(0xFF9013FE) // Un morado claro para texto

// 2. Crea el esquema de colores para el modo oscuro/claro
private val DarkColorPalette = darkColorScheme(
    primary = CustomGreen,        // Color principal para botones, indicadores, etc.
    onPrimary = Color.Black,      // Color del texto/iconos sobre el color primario
    secondary = CustomGreen,      // Color secundario
    onSecondary = Color.Black,    // Texto/iconos sobre el color secundario
    surface = Color(0xFF1C1C1E),  // Color de fondo de superficies como cards, nav bars
    onSurface = CustomPurple,     // Color principal del texto sobre fondos
    background = Color(0xFF000000), // Color de fondo general de la app
    onBackground = CustomPurple   // Color del texto sobre el fondo general
)

// 3. Crea el Composable del Tema que envolverá tu app
@Composable
fun AppPruebaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorPalette,
        // Aquí puedes definir tipografía y formas también
        content = content
    )
}