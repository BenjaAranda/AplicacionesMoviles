package com.example.app_prueba.ui.screens.account

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_prueba.navigation.Routes
import com.example.app_prueba.ui.components.Footer
import com.example.app_prueba.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavController, viewModel: ProfileViewModel = viewModel()) {
    val user by viewModel.user.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mi Cuenta", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = { Footer(navController) }
    ) { paddingValues ->
        // Usamos Column principal para organizar: Contenido Arriba (flexible) + Botón Abajo (fijo)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- BLOQUE SUPERIOR (Información) ---
            // Le damos weight(1f) para que ocupe todo el espacio disponible,
            // empujando el botón hacia abajo, pero sin aplastarlo.
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()), // Hacemos scrollable por si la pantalla es chica
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Información de Usuario",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(32.dp))

                if (user != null) {
                    InfoRow(label = "Nombre", value = user?.name ?: "Sin nombre")
                    Divider(modifier = Modifier.padding(vertical = 12.dp))
                    InfoRow(label = "Email", value = user?.email ?: "")
                    Divider(modifier = Modifier.padding(vertical = 12.dp))
                    InfoRow(label = "ID Usuario", value = user?.id.toString())

                    if (user?.hasDuocDiscount == true) {
                        Divider(modifier = Modifier.padding(vertical = 12.dp))
                        Text(
                            "¡Tienes descuento DUOC activo!",
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Text("No hay sesión activa", color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Inicia sesión para ver tus datos.", fontSize = 14.sp, color = Color.Gray)
                }
            }

            // --- BLOQUE INFERIOR (Botón) ---
            // Al estar fuera del Column con weight, su altura es sagrada y no se aplasta.
            Spacer(modifier = Modifier.height(16.dp)) // Separación segura

            if (user != null) {
                Button(
                    onClick = {
                        viewModel.logout()
                        navController.navigate(Routes.Login.route) { popUpTo(0) }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD32F2F), // Rojo fuerte manual
                        contentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp) // Altura forzada y generosa
                ) {
                    Text("Cerrar Sesión", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = { navController.navigate(Routes.Login.route) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E7D32), // Verde fuerte manual
                        contentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Text("Iniciar Sesión", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}