package com.example.app_prueba.viewmodel

import android.app.Application
import com.example.app_prueba.application.LevelUpGamerApp
import com.example.app_prueba.repository.UserRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.Every
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {

    @RelaxedMockK
    private lateinit var loginViewModel: LoginViewModel

    @RelaxedMockK
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        val app = mockk<LevelUpGamerApp>(relaxed = true)

        loginViewModel = LoginViewModel(application = app)

        val fieldRepo = LoginViewModel::class.java.getDeclaredField("repository")
        fieldRepo.isAccessible = true
        fieldRepo.set(loginViewModel, userRepository)
    }

    @Test
    fun whenEmailAndPasswordAreNotEntered() = runBlocking {

        loginViewModel.email = ""
        loginViewModel.pass = ""

        // When

        loginViewModel.onLogin(onSuccess = {
            fail("El login no debería ser exitoso con campos vacíos")
        })

        // Then
        assertEquals("Correo y contraseña obligatorios.", loginViewModel.loginError)


        assertFalse("No debería estar cargando", loginViewModel.isLoading)
    }

    @Test
    fun whenTheEmailAndPasswordAreValid() = runBlocking {
        // --- GIVEN (DADO) ---
        val testEmail = "estudiante@duocuc.cl"
        val testPass = "123456"

        loginViewModel.email = testEmail
        loginViewModel.pass = testPass

        // 1. PREPARAR LA RESPUESTA FALSA (MOCK DATA)

        // CORRECCIÓN 1: La clase User requiere 'pass'.
        // Aunque sea un mock, el constructor de Kotlin lo exige.
        val mockUserBackend = com.example.app_prueba.data.model.User(
            id = 1, // Opcional, tiene default 0, pero lo pongo explícito
            email = testEmail,
            name = "Juan Pérez",
            pass = "password_encriptada_falsa", // <-- Faltaba esto
            hasDuocDiscount = true
        )

        // CORRECCIÓN 2: Usar AuthData en vez de LoginData
        val mockData = com.example.app_prueba.data.model.AuthData(
            token = "token_falso_12345",
            user = mockUserBackend
        )

        // CORRECCIÓN 3: Usar AuthResponse en vez de LoginResponse
        val mockResponse = com.example.app_prueba.data.model.AuthResponse(
            success = true,
            message = "Login Exitoso",
            data = mockData
        )

        // 2. ENTRENAR AL REPOSITORIO (MOCKING)
        // Asegúrate de que tu repositorio devuelva Response<AuthResponse>
        io.mockk.coEvery { userRepository.loginUser(any()) } returns retrofit2.Response.success(
            mockResponse
        )

        // Variable "chivata"
        var successCalled = false

        // --- WHEN (CUANDO) ---
        loginViewModel.onLogin(onSuccess = {
            successCalled = true
        })

        // --- THEN (ENTONCES) ---
        Assert.assertNull("No debería haber mensaje de error", loginViewModel.loginError)
        Assert.assertFalse("El loading debería haber terminado", loginViewModel.isLoading)
    }
}