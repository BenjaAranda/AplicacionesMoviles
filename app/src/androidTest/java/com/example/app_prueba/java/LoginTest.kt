package com.example.app_prueba.java

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.app_prueba.ui.screens.login.LoginScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginTest {

    @get:Rule
    val rule = createComposeRule()

    @Before
    fun setUp() {
        rule.setContent {
            val navController = rememberNavController()
            LoginScreen(navController = navController)
        }
    }

    @Test
    fun loginScreen_successFlow() {
        // Espera inicial
        Thread.sleep(2000)

        // Introduce email correcto
        rule.onNodeWithTag("email", useUnmergedTree = true)
            .performTextInput("exsairs@gmail.com")

        // Pausa
        Thread.sleep(2000)

        // Introduce contraseña correcta
        rule.onNodeWithTag("password", useUnmergedTree = true)
            .performTextInput("Dreikdreik1")

        // Pausa
        Thread.sleep(2000)

        // Clic en el botón
        rule.onNodeWithTag("loginButton", useUnmergedTree = true).performClick()

        // En una prueba de UI aislada, la navegación no ocurre.
        // La verificación es simplemente que la app no crashea.
        Thread.sleep(2000)
    }

    // --- NUEVA PRUEBA PARA LOGIN INCORRECTO ---
    @Test
    fun loginScreen_failureFlow_wrongCredentials() {
        // Espera inicial de 2 segundos.
        Thread.sleep(2000)

        // --- PASO 1: INTRODUCIR EMAIL INCORRECTO ---
        rule.onNodeWithTag("email", useUnmergedTree = true)
            .performTextInput("gmail.prueba@gmail.com") // Email incorrecto

        // Pausa de 2 segundos.
        Thread.sleep(2000)

        // --- PASO 2: INTRODUCIR CONTRASEÑA ---
        rule.onNodeWithTag("password", useUnmergedTree = true)
            .performTextInput("CualquierPass")

        // Pausa de 2 segundos.
        Thread.sleep(2000)

        // --- PASO 3: HACER CLIC EN EL BOTÓN DE LOGIN ---
        rule.onNodeWithTag("loginButton", useUnmergedTree = true).performClick()

        // Pausa para dar tiempo a que el ViewModel procese y muestre el error.
        Thread.sleep(2000)

        // --- PASO 4: VERIFICAR QUE EL MENSAJE DE ERROR APARECE ---
        // Buscamos el texto de error por el nuevo testTag y verificamos que es visible.
        val errorText = rule.onNodeWithTag("loginErrorText", useUnmergedTree = true)
        errorText.assertIsDisplayed()
    }
}
