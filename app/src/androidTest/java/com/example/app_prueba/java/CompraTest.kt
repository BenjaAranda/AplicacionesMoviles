package com.example.app_prueba.java

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.example.app_prueba.MainActivity
import com.example.app_prueba.application.LevelUpGamerApp
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CompraTest {

    // Usamos createAndroidComposeRule para lanzar la Activity principal y probar el flujo completo.
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    // Esta función se ejecuta antes de cada prueba para limpiar el carrito.
    @Before
    fun clearCartBeforeTest() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val db = (context.applicationContext as LevelUpGamerApp).database
        // runBlocking se usa para ejecutar una corutina en un contexto de prueba no-corutina.
        runBlocking {
            db.cartDao().clearAllItems()
        }
    }

    @Test
    fun fullPurchaseFlow_login_addProduct_verifyCart() {
        // Pausa inicial para que la app se cargue
        Thread.sleep(2000)


        // Introduce email
        rule.onNodeWithTag("email").performTextInput("exsairs@gmail.com")
        Thread.sleep(1000)
        // Introduce contraseña
        rule.onNodeWithTag("password").performTextInput("Dreikdreik1")
        Thread.sleep(1000)
        // Clic en el botón de login
        rule.onNodeWithTag("loginButton").performClick()

        // --- PASO 2: AÑADIR PRODUCTO AL CARRITO ---
        // Esperamos a que la pantalla de inicio/productos se cargue después del login.
        // El test esperará automáticamente a que la UI esté inactiva.
        rule.waitUntil(timeoutMillis = 10000) {
            rule.onAllNodesWithTag("addToCart_", substring = true).fetchSemanticsNodes().isNotEmpty()
        }

        // Seleccionamos el primer producto que encontremos y lo añadimos al carrito.
        // Supongamos que el producto tiene el código "PROD001". AJUSTA este código al de un producto real.
        val productCodeToAdd = "PROD001" // <--- ¡¡¡IMPORTANTE: CAMBIA ESTO!!!
        rule.onNodeWithTag("addToCart_$productCodeToAdd").performScrollTo().performClick()

        // Pausa para ver la acción
        Thread.sleep(2000)

        // --- PASO 3: IR AL CARRITO DE COMPRAS ---
        // Buscamos el botón para ir al carrito y hacemos clic.
        rule.onNodeWithTag("goToCartButton").performClick()

        // --- PASO 4: VERIFICAR EL CONTENIDO DEL CARRITO ---
        // Esperamos a que la pantalla del carrito se cargue.
        rule.waitUntil(timeoutMillis = 5000) {
            rule.onAllNodesWithTag("cartScreenTitle").fetchSemanticsNodes().isNotEmpty()
        }

        // Verificamos que el título "Mi Carrito" esté visible.
        rule.onNodeWithTag("cartScreenTitle").assertIsDisplayed()

        // Verificamos que el producto que añadimos ("PROD001") existe en el carrito.
        rule.onNodeWithTag("cartItem_$productCodeToAdd").assertIsDisplayed()

        // Verificamos que el botón "IR A PAGAR" esté visible y habilitado.
        rule.onNodeWithTag("goToCheckoutButton").assertIsDisplayed().assertIsEnabled()

        // Pausa final para observar el resultado
        Thread.sleep(3000)
    }
}
