package com.example.app_prueba.ui.screens.about

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.app_prueba.ui.components.Footer

@Composable
fun AboutUsScreen(navController: NavController) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Box(
                modifier = Modifier
                    .fillParentMaxHeight(0.9f) // Ocupa la mayor parte de la pantalla visible
                    .fillParentMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("Página de Nosotros")
            }
        }
        item {
            Footer(navController = navController)
        }
    }
}