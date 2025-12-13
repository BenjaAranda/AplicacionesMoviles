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

}