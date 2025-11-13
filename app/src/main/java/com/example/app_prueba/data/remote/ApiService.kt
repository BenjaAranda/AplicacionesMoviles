package com.example.app_prueba.data.remote

import com.example.app_prueba.data.model.AuthResponse
import com.example.app_prueba.data.model.UserRegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    // Define el endpoint POST en la ruta "register"
    // Esto llamará a http://10.0.2.2:5000/api/register
    @POST("register")
    suspend fun registerUser(
        @Body user: UserRegisterRequest
    ): Response<AuthResponse>
}