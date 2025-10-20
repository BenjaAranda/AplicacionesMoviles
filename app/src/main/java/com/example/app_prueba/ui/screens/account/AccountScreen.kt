package com.example.app_prueba.ui.screens.account

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.LaunchedEffect // <-- IMPORT AÑADIDO
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState // <-- IMPORT AÑADIDO
import com.example.app_prueba.navigation.Routes
import com.example.app_prueba.viewmodel.ProfileViewModel
import com.example.app_prueba.viewmodel.SessionViewModel

val DarkBackground = Color(0xFF000000)
val NeonGreen = Color(0xFF39FF14)
val NeonPurple = Color(0xFF9D00FF)
val TextColor = Color.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavController, profileViewModel: ProfileViewModel = viewModel()) {
    val isLoggedIn = SessionViewModel.isLoggedIn
    val currentUserEmail = SessionViewModel.currentUserEmail

    if (isLoggedIn && currentUserEmail != null) {

        var selectedOption by remember { mutableStateOf<String?>(null) }

        // --- CÓDIGO AÑADIDO PARA RESETEAR ---
        // 1. Observa la pila de navegación
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        // 2. Obtiene la ruta actual
        val currentRoute = navBackStackEntry?.destination?.route

        // 3. Este efecto se ejecuta cada vez que 'currentRoute' cambia
        LaunchedEffect(currentRoute) {
            // Si la ruta actual es la de esta pantalla (Account), resetea la selección
            // ¡¡IMPORTANTE!! Asumo que tu ruta se llama 'Routes.Account.route'
            // Si tiene otro nombre en tu 'Routes', ajústalo aquí.
            if (currentRoute == Routes.Account.route) {
                selectedOption = null
            }
        }
        // --- FIN DEL CÓDIGO AÑADIDO ---

        Scaffold(
            containerColor = DarkBackground,
            topBar = {
                TopAppBar(
                    title = { Text(text = "Mi Perfil", color = TextColor) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = DarkBackground,
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
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    text = "¡Hola, $currentUserEmail!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextColor
                )
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ProfileOptionItem(
                        text = "Datos Personales",
                        icon = Icons.Default.Person,
                        color = if (selectedOption == "Datos Personales") NeonGreen else NeonPurple,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedOption = "Datos Personales"
                            // TODO: navController.navigate(Routes.PersonalData.route)
                        }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    ProfileOptionItem(
                        text = "Editar Perfil",
                        icon = Icons.Default.Edit,
                        color = if (selectedOption == "Editar Perfil") NeonGreen else NeonPurple,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedOption = "Editar Perfil"
                            // TODO: navController.navigate(Routes.EditProfile.route)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ProfileOptionItem(
                        text = "Mis Pedidos",
                        icon = Icons.Default.ShoppingCart,
                        color = if (selectedOption == "Mis Pedidos") NeonGreen else NeonPurple,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedOption = "Mis Pedidos"
                            navController.navigate(Routes.Cart.route)
                        }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    ProfileOptionItem(
                        text = "Seguimiento de Pedido",
                        icon = Icons.Default.Download,
                        color = if (selectedOption == "Seguimiento de Pedido") NeonGreen else NeonPurple,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedOption = "Seguimiento de Pedido"
                            navController.navigate(Routes.Home.route)
                        }   // TODO: navController.navigate(Routes.OrderTracking.route)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ProfileOptionItem(
                        text = "Gestión de Cuenta",
                        icon = Icons.Default.Settings,
                        color = if (selectedOption == "Gestión de Cuenta") NeonGreen else NeonPurple,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedOption = "Gestión de Cuenta"
                            // TODO: navController.navigate(Routes.AccountManagement.route)
                        }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    ProfileOptionItem(
                        text = "Cambiar Tarjeta",
                        icon = Icons.Default.CreditCard,
                        color = if (selectedOption == "Cambiar Tarjeta") NeonGreen else NeonPurple,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedOption = "Cambiar Tarjeta"
                            // TODO: navController.navigate(Routes.PaymentMethods.route)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ProfileOptionItem(
                        text = "Puntos",
                        icon = Icons.Default.Star,
                        color = if (selectedOption == "Puntos") NeonGreen else NeonPurple,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedOption = "Puntos"
                            navController.navigate(Routes.Points.route)
                        }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    ProfileOptionItem(
                        text = "Soporte Técnico",
                        icon = Icons.Default.SupportAgent,
                        color = if (selectedOption == "Soporte Técnico") NeonGreen else NeonPurple,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedOption = "Soporte Técnico"
                            navController.navigate(Routes.Contact.route)
                        }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { /* TODO: Mostrar diálogo de confirmación */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
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

                Button(
                    onClick = { profileViewModel.onLogout(navController) },
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
    else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Únete a la Comunidad",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Inicia sesión o crea una cuenta para acceder a tu carrito y perfil.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate(Routes.Login.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = { navController.navigate(Routes.Register.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear Cuenta")
            }
        }
    }
}


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
            .aspectRatio(1.5f)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = DarkBackground,
        border = BorderStroke(2.dp, color)
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
                tint = color,
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