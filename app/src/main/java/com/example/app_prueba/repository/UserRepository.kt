package com.example.app_prueba.repository

import com.example.app_prueba.data.model.AuthResponse
import com.example.app_prueba.data.model.UserRegisterRequest
import com.example.app_prueba.data.remote.RetrofitInstance
import retrofit2.Response

class UserRepository {

    private val api = RetrofitInstance.api

    suspend fun registerUser(user: UserRegisterRequest): Response<AuthResponse> {
        return api.registerUser(user)
    }
}