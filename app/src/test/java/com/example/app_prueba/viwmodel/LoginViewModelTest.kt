package com.example.app_prueba.viewmodel

import android.app.Application
import com.example.app_prueba.application.LevelUpGamerApp
import com.example.app_prueba.data.database.AppDatabase
import com.example.app_prueba.data.database.UserDao
import com.example.app_prueba.data.model.AuthData
import com.example.app_prueba.data.model.AuthResponse
import com.example.app_prueba.data.model.User
import com.example.app_prueba.repository.UserRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private lateinit var loginViewModel: LoginViewModel

    @MockK
    lateinit var userRepository: UserRepository

    @MockK
    lateinit var application: LevelUpGamerApp

    @MockK
    lateinit var database: AppDatabase

    @MockK
    lateinit var userDao: UserDao

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        // 1. Configuramos el hilo principal para pruebas
        Dispatchers.setMain(Dispatchers.Unconfined)

        // 2. Mock de la Base de Datos
        every { application.database } returns database
        every { database.userDao() } returns userDao
        // Simulamos que la inserción en BD funciona y no hace nada (returns Unit)
        coEvery { userDao.insert(any()) } returns Unit

        // 3. Mock del Singleton de Sesión
        mockkObject(SessionViewModel)
        every { SessionViewModel.onLoginSuccess(any(), any(), any()) } returns Unit

        // 4. Inicializamos el ViewModel
        loginViewModel = LoginViewModel(application)

        // 5. Inyectamos el Repositorio Falso
        // (Esto funciona porque cambiaste la variable a 'var' en el ViewModel)
        loginViewModel.userRepository = userRepository
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkObject(SessionViewModel)
    }

    @Test
    fun whenEmailAndPasswordAreNotEntered() {
        // Given
        loginViewModel.email = ""
        loginViewModel.pass = ""

        // When
        loginViewModel.onLogin(onSuccess = {
            fail("El login no debería ser exitoso con campos vacíos")
        })

        // Then
        // CORREGIDO: Actualizamos el texto esperado
        assertEquals("Correo y contraseña obligatorios.", loginViewModel.loginError)
        assertFalse("No debería estar cargando", loginViewModel.isLoading)
    }

    @Test
    fun whenTheEmailAndPasswordAreValid() {
        // Given
        val testEmail = "estudiante@duocuc.cl"
        val testPass = "123456"

        loginViewModel.email = testEmail
        loginViewModel.pass = testPass

        // Preparar respuesta falsa (Mock)
        val mockUserBackend = User(
            id = 1,
            email = testEmail,
            name = "Juan Pérez",
            pass = "pass_falsa",
            hasDuocDiscount = true
        )

        val mockData = AuthData(
            token = "token_falso_12345",
            user = mockUserBackend
        )

        val mockResponse = AuthResponse(
            success = true,
            message = "Login Exitoso",
            data = mockData
        )

        // Entrenamos al repositorio para devolver éxito
        coEvery { userRepository.loginUser(any()) } returns Response.success(mockResponse)

        var successCalled = false

        // When
        loginViewModel.onLogin(onSuccess = {
            successCalled = true
        })

        // Then
        // VERIFICACIÓN MEJORADA: Si falla, te imprimirá el error real del ViewModel
        if (!successCalled) {
            println("ERROR DETECTADO EN VIEWMODEL: ${loginViewModel.loginError}")
        }

        assertTrue(
            "El login falló. Mensaje de error interno: ${loginViewModel.loginError}",
            successCalled
        )

        assertNull("No debería haber mensaje de error almacenado", loginViewModel.loginError)
        assertFalse("El loading debería haber terminado", loginViewModel.isLoading)
    }
}