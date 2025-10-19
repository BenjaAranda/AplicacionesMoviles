package com.example.app_prueba.ui.screens.profile


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_prueba.viewmodel.ProfileViewModel

// --- Definimos los colores de tu tema neon ---
val DarkBackground = Color(0xFF000000) // Fondo negro
val NeonGreen = Color(0xFF39FF14) // Verde neón para destacar
val NeonPurple = Color(0xFF9D00FF) // Morado neón para el resto
val TextColor = Color.White // Texto blanco

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, vm: ProfileViewModel = viewModel()) {
    val context = LocalContext.current

    Scaffold(
        containerColor = DarkBackground, // Aplicamos el fondo oscuro
        topBar = {
            TopAppBar(
                title = { Text(text = "Mi Perfil", color = TextColor) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás",
                            tint = TextColor // Icono en blanco
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground, // Color de la barra
                    scrolledContainerColor = DarkBackground
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                // Añadimos scroll vertical por si el contenido crece
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            // --- Cuadrícula de Opciones (estilo Buscalibre) ---
            // Usamos el NeonGreen para "Datos Personales" como en tu web

            // Fila 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProfileOptionItem(
                    text = "Datos Personales",
                    icon = Icons.Default.Person,
                    color = NeonGreen, // Color destacado
                    modifier = Modifier.weight(1f),
                    onClick = { /* TODO: Navegar a Datos Personales */ }
                )
                Spacer(modifier = Modifier.width(16.dp))
                ProfileOptionItem(
                    text = "Editar Perfil",
                    icon = Icons.Default.Edit,
                    color = NeonPurple,
                    modifier = Modifier.weight(1f),
                    onClick = { /* TODO: Navegar a Editar Perfil */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fila 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProfileOptionItem(
                    text = "Mis Pedidos",
                    icon = Icons.Default.ShoppingCart,
                    color = NeonPurple,
                    modifier = Modifier.weight(1f),
                    onClick = { /* TODO: Navegar a Pedidos */ }
                )
                Spacer(modifier = Modifier.width(16.dp))
                ProfileOptionItem(
                    text = "Productos Digitales",
                    icon = Icons.Default.Download,
                    color = NeonPurple,
                    modifier = Modifier.weight(1f),
                    onClick = { /* TODO: Navegar a Productos Digitales */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fila 3
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProfileOptionItem(
                    text = "Gestión de Cuenta",
                    icon = Icons.Default.Settings,
                    color = NeonPurple,
                    modifier = Modifier.weight(1f),
                    onClick = { /* TODO: Navegar a Gestión de Cuenta */ }
                )
                Spacer(modifier = Modifier.width(16.dp))
                ProfileOptionItem(
                    text = "Cambiar Tarjeta",
                    icon = Icons.Default.CreditCard,
                    color = NeonPurple,
                    modifier = Modifier.weight(1f),
                    onClick = { /* TODO: Navegar a Cambiar Tarjeta */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fila 4
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProfileOptionItem(
                    text = "Puntos",
                    icon = Icons.Default.Star,
                    color = NeonPurple,
                    modifier = Modifier.weight(1f),
                    onClick = { /* TODO: Navegar a Puntos */ }
                )
                Spacer(modifier = Modifier.width(16.dp))
                ProfileOptionItem(
                    text = "Soporte Técnico",
                    icon = Icons.Default.SupportAgent,
                    color = NeonPurple,
                    modifier = Modifier.weight(1f),
                    onClick = { vm.contactSupport(context) } // Acción que ya tenías
                )
            }

            // Empuja los botones de sesión al fondo
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(24.dp)) // Espacio extra antes de los botones

            // --- Botones de Cuenta al final (como en Buscalibre) ---

            // Botón Eliminar Cuenta (con borde, menos énfasis)
            Button(
                onClick = { /* TODO: Mostrar diálogo de confirmación */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent, // Sin relleno
                    contentColor = MaterialTheme.colorScheme.error
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Eliminar Cuenta")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón Cerrar Sesión (con relleno, más énfasis)
            Button(
                onClick = { vm.onLogout(navController) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Cerrar Sesión",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Cerrar Sesión")
            }
        }
    }
}

/**
 * Composable reutilizable para cada "botón" de la cuadrícula.
 * Es una tarjeta con un borde de color neón.
 */
@Composable
fun ProfileOptionItem(
    text: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .aspectRatio(1.5f) // Rectangular, (1f) para cuadrado
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = DarkBackground, // Mismo fondo
        border = BorderStroke(2.dp, color) // Borde con el color neón
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = color, // Icono con color neón
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                color = TextColor,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}
