package com.example.app_prueba.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_prueba.navigation.Routes
// --- INICIO DE IMPORTACIONES DE COLOR ---
import com.example.app_prueba.ui.theme.MoradoNeon
import com.example.app_prueba.ui.theme.VerdeNeon
// --- FIN DE IMPORTACIONES DE COLOR ---
import com.example.app_prueba.viewmodel.LoginViewModel

@Composable
fun LoginScreen(navController: NavController, vm: LoginViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        // --- Colores para TextField ---
        val customTextFieldColors = TextFieldDefaults.colors(
            focusedTextColor = MoradoNeon,
            unfocusedTextColor = MoradoNeon,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = VerdeNeon,
            unfocusedIndicatorColor = VerdeNeon,
            cursorColor = VerdeNeon,
            focusedLabelColor = MoradoNeon,
            unfocusedLabelColor = VerdeNeon
        )

        // --- INICIO DE CAMBIOS DE COLOR DEL BOTÓN ---
        val customButtonColors = ButtonDefaults.buttonColors(
            containerColor = MoradoNeon, // Color de fondo normal
            contentColor = Color.White,  // Color del texto
            // Aunque no lo pediste, es bueno definir el color presionado,
            // pero Material 3 ya lo hace por defecto oscureciendo el `containerColor`.
            // Para un cambio explícito a verde, necesitarías manejar la interacción.
            // Por simplicidad, dejaremos el morado que se oscurece al presionar.
            // Si quieres un cambio total a verde, el código sería más complejo.
        )
        // --- FIN DE CAMBIOS DE COLOR DEL BOTÓN ---

        OutlinedTextField(
            value = vm.email,
            onValueChange = { vm.email = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth(),
            colors = customTextFieldColors
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = vm.pass,
            onValueChange = { vm.pass = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = customTextFieldColors
        )
        Spacer(modifier = Modifier.height(16.dp))

        vm.loginError?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                vm.onLogin {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = customButtonColors // Aplicar colores al botón
        ) {
            Text("Entrar")
        }

        TextButton(onClick = { navController.navigate(Routes.Register.route) }) {
            Text("¿No tienes cuenta? Regístrate", color = MoradoNeon)

        }
    }
}
