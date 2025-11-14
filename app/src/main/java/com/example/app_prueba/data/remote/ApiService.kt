package com.example.app_prueba.data.remote

import com.example.app_prueba.data.model.AuthResponse
import com.example.app_prueba.data.model.UserRegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    suspend fun registerUser(@Body user: UserRegisterRequest): Response<AuthResponse>
}
