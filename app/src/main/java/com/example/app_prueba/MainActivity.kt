// ruta: MainActivity.kt
package com.example.app_prueba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.app_prueba.navigation.AppNavigation
import com.example.app_prueba.ui.theme.LevelUpGamerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LevelUpGamerTheme {
                // Un contenedor de superficie que usa el color de fondo de nuestro tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Aquí llamamos a toda la navegación y las pantallas que hemos creado
                    AppNavigation()
                }
            }
        }
    }
}