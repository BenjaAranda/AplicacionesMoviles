package com.example.app_prueba.ui.screens.blog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.app_prueba.ui.components.Footer

@Composable
fun BlogScreen(navController: NavController) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Box(
                modifier = Modifier
                    .fillParentMaxHeight(0.9f)
                    .fillParentMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("Página del Blog")
            }
        }
        item {
            Footer(navController = navController)
        }
    }
}