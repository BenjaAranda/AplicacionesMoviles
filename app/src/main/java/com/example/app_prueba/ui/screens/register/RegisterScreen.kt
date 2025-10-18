package com.example.app_prueba.ui.screens.register

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
import com.example.app_prueba.ui.theme.MoradoNeon // Importar color
import com.example.app_prueba.ui.theme.VerdeNeon   // Importar color
import com.example.app_prueba.viewmodel.RegisterViewModel

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

        // --- Colores para Checkbox ---
        val customCheckboxColors = CheckboxDefaults.colors(
            checkedColor = VerdeNeon,
            uncheckedColor = MoradoNeon,
            checkmarkColor = MoradoNeon
        )

        // --- INICIO DE CAMBIOS DE COLOR DEL BOTÓN ---
        val customButtonColors = ButtonDefaults.buttonColors(
            containerColor = MoradoNeon, // Color de fondo normal del botón
            contentColor = Color.White    // Color del texto del botón
        )
        // --- FIN DE CAMBIOS DE COLOR DEL BOTÓN ---

        OutlinedTextField(
            value = vm.email,
            onValueChange = { vm.email = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth(),
            isError = vm.registerError != null,
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

        OutlinedTextField(
            value = vm.confirmPass,
            onValueChange = { vm.confirmPass = it },
            label = { Text("Confirmar Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = customTextFieldColors
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Checkbox para mayor de edad
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = vm.isOfLegalAge,
                onCheckedChange = { vm.isOfLegalAge = it },
                colors = customCheckboxColors
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Soy mayor de edad")
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Checkbox para estudiante Duoc
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = vm.isDuocStudent,
                onCheckedChange = { vm.isDuocStudent = it },
                colors = customCheckboxColors
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Soy estudiante Duoc")
        }
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
            colors = customButtonColors // Aplicar colores al botón
        ) {
            Text("Registrarse")
        }

        TextButton(onClick = { navController.popBackStack() }) {
            // Aplicar color al texto del TextButton
            Text("¿Ya tienes cuenta? Inicia Sesión", color = MoradoNeon)
        }
    }
}
