package com.example.app_prueba.java

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.app_prueba.ui.screens.register.RegisterScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID

class RegisterTest {

    @get:Rule
    val rule = createComposeRule()

    @Before
    fun setUp() {
        // Lanzamos directamente la RegisterScreen en nuestro lienzo de prueba.
        rule.setContent {
            // Como RegisterScreen necesita un NavController, le pasamos uno de prueba.
            val navController = rememberNavController()
            RegisterScreen(navController = navController)
        }
    }

    @Test
    fun registerScreen_successFlow() {
        // Generamos un email único para cada ejecución de la prueba para evitar errores de "usuario ya existe".
        val uniqueEmail = "testuser_${UUID.randomUUID()}@test.com"

        // Pausa inicial
        Thread.sleep(2000)

        // --- PASO 1: RELLENAR DATOS ---
        rule.onNodeWithTag("registerEmail").performTextInput(uniqueEmail)
        Thread.sleep(1000) // Pausa corta
        rule.onNodeWithTag("registerPassword").performTextInput("password123")
        Thread.sleep(1000) // Pausa corta
        rule.onNodeWithTag("registerConfirmPassword").performTextInput("password123")
        Thread.sleep(1000) // Pausa corta

        // El checkbox "mayor de edad" ya está marcado por defecto, así que no hacemos nada.

        // --- PASO 2: HACER CLIC EN REGISTRARSE ---
        rule.onNodeWithTag("registerButton").performClick()

        // --- PASO 3: VERIFICACIÓN ---
        // En una prueba de UI aislada, la navegación real no ocurre.
        // La prueba es exitosa si no crashea y no aparece ningún mensaje de error.
        Thread.sleep(2000)
        // Verificamos que el texto de error NO exista.
        rule.onNodeWithTag("registerErrorText").assertDoesNotExist()
    }

    @Test
    fun registerScreen_failure_passwordsDoNotMatch() {
        // Pausa inicial
        Thread.sleep(2000)

        // --- PASO 1: RELLENAR DATOS CON CONTRASEÑAS DIFERENTES ---
        rule.onNodeWithTag("registerEmail").performTextInput("test@test.com")
        Thread.sleep(1000) // Pausa corta
        rule.onNodeWithTag("registerPassword").performTextInput("password123")
        Thread.sleep(1000) // Pausa corta
        rule.onNodeWithTag("registerConfirmPassword").performTextInput("password456") // Contraseña incorrecta
        Thread.sleep(1000) // Pausa corta

        // --- PASO 2: HACER CLIC EN REGISTRARSE ---
        rule.onNodeWithTag("registerButton").performClick()

        // --- PASO 3: VERIFICAR QUE APARECE EL MENSAJE DE ERROR ---
        Thread.sleep(2000) // Espera a que el ViewModel procese el error.

        val errorText = rule.onNodeWithTag("registerErrorText")
        errorText.assertIsDisplayed() // Verificamos que el nodo con el error es visible.
    }
}
