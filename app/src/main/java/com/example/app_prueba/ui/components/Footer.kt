package com.example.app_prueba.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app_prueba.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Footer(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("LEVEL-UP GAMER", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
        Text("Tu tienda gamer en Chile desde 2022", fontSize = 12.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        Text("Enlaces", fontWeight = FontWeight.Bold, color = Color.White)
        Text("Home", modifier = Modifier.clickable { navController.navigate(Routes.Home.route) }, fontSize = 14.sp, color = Color.Gray)
        Text("Productos", modifier = Modifier.clickable { navController.navigate(Routes.Products.route) }, fontSize = 14.sp, color = Color.Gray)
        Text("Nosotros", modifier = Modifier.clickable { navController.navigate(Routes.AboutUs.route) }, fontSize = 14.sp, color = Color.Gray)
        Text("Blog", modifier = Modifier.clickable { navController.navigate(Routes.Blog.route) }, fontSize = 14.sp, color = Color.Gray)
        Text("Contacto", modifier = Modifier.clickable { navController.navigate(Routes.Contact.route) }, fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "© 2025 Level-Up Gamer. Todos los derechos reservados.",
            fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center
        )
    }
}