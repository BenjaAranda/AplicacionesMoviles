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
import io.mockk.coVerify
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
class RegisterViewModelTest {

    // Quitamos la Rule de InstantTaskExecutor porque usamos Compose, no LiveData

    @MockK
    lateinit var userRepository: UserRepository

    @MockK
    lateinit var application: LevelUpGamerApp

    @MockK
    lateinit var database: AppDatabase

    @MockK
    lateinit var userDao: UserDao

    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        // 1. CONTROLAMOS EL TIEMPO (Corutinas)
        Dispatchers.setMain(Dispatchers.Unconfined)

        // 2. CONFIGURAMOS LA BASE DE DATOS FALSA
        every { application.database } returns database
        every { database.userDao() } returns userDao

        // Mockeamos inserción en BD para que no falle
        coEvery { userDao.insert(any()) } returns Unit

        // 3. INYECTAMOS EL REPOSITORIO FALSO
        // Aquí es donde ocurre la magia: le pasamos 'userRepository' (el mock) al ViewModel
        viewModel = RegisterViewModel(application, userRepository)

        // Mockeamos SessionViewModel (singleton)
        mockkObject(SessionViewModel)
        every { SessionViewModel.onLoginSuccess(any(), any(), any()) } returns Unit
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkObject(SessionViewModel)
    }

    @Test
    fun `when API returns success false, should show error message`() {
        // GIVEN
        viewModel.email = "existe@gmail.com"
        viewModel.pass = "123456"
        viewModel.confirmPass = "123456"
        viewModel.isOfAge = true

        // Entrenamos al mock: "Cuando llamen a registerUser, responde que falló"
        val mockResponse = AuthResponse(
            success = false,
            message = "El correo ya está registrado",
            data = null
        )
        coEvery { userRepository.registerUser(any()) } returns Response.success(mockResponse)

        // WHEN
        viewModel.onRegister {}

        // THEN
        assertEquals("El correo ya está registrado", viewModel.registerError)
    }

    @Test
    fun `when register is successful via API, should save to DB and call onSuccess`() {
        // GIVEN
        val testEmail = "nuevo@duocuc.cl"
        val testPass = "123456"

        viewModel.email = testEmail
        viewModel.pass = testPass
        viewModel.confirmPass = testPass
        viewModel.name = "Pedro"
        viewModel.isOfAge = true

        // Entrenamos al mock: "Responde ÉXITO"
        val mockUser = User(email = testEmail, pass = testPass, name = "Pedro", hasDuocDiscount = true)
        val mockData = AuthData(token = "token_xyz", user = mockUser)
        val mockResponse = AuthResponse(success = true, message = "OK", data = mockData)

        coEvery { userRepository.registerUser(any()) } returns Response.success(mockResponse)

        var successCalled = false

        // WHEN
        viewModel.onRegister(onSuccess = {
            successCalled = true
        })

        // THEN
        assertTrue("Debió llamar a onSuccess", successCalled)
        assertNull("No debe haber error", viewModel.registerError)

        // Verificar que intentó guardar en la BD
        coVerify(exactly = 1) { userDao.insert(any()) }
    }
}