package com.example.app_prueba.repository

import com.example.app_prueba.data.model.AuthResponse
import com.example.app_prueba.data.model.LoginRequest
import com.example.app_prueba.data.model.UserRegisterRequest
import com.example.app_prueba.data.remote.RetrofitInstance
import retrofit2.Response
import java.io.IOException

class UserRepository {

    private val api = RetrofitInstance.api

    suspend fun registerUser(user: UserRegisterRequest): Response<AuthResponse> {
        return api.registerUser(user)
    }

    suspend fun loginUser(request: LoginRequest): Response<AuthResponse> {
        return api.loginUser(request)
    }

    // Puedes añadir más funciones (products, cart, orders) aquí.
}
