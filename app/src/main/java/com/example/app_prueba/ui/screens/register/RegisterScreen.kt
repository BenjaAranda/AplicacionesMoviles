package com.example.app_prueba.ui.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_prueba.navigation.Routes
import com.example.app_prueba.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, vm: RegisterViewModel = viewModel()) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Crear Cuenta", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))


        val customTextFieldColors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,      // Borde enfocado: Verde
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), // Borde sin foco: Morado tenue
            cursorColor = MaterialTheme.colorScheme.primary,             // Cursor: Verde
            focusedLabelColor = MaterialTheme.colorScheme.primary,       // Etiqueta enfocada: Verde
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,   // Etiqueta sin foco: Morado
        )


        val primaryButtonColors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,      // Fondo del botón: Verde
            contentColor = MaterialTheme.colorScheme.onPrimary,      // Color del texto: Negro (definido en onPrimary)
        )

        OutlinedTextField(
            value = vm.email,
            onValueChange = { vm.email = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth(),
            colors = customTextFieldColors // Aplicamos colores
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = vm.pass,
            onValueChange = { vm.pass = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = customTextFieldColors // Aplicamos colores
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = vm.confirmPass,
            onValueChange = { vm.confirmPass = it },
            label = { Text("Confirmar Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = customTextFieldColors // Aplicamos colores
        )
        Spacer(modifier = Modifier.height(16.dp))

        vm.registerError?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                vm.onRegister {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = primaryButtonColors // Aplicamos colores
        ) {
            Text("Registrarse")
        }

        TextButton(onClick = { navController.popBackStack() }) {
            Text("¿Ya tienes cuenta? Inicia Sesión")
        }
    }
}
