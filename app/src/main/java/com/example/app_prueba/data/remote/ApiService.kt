// ruta: data/remote/ApiService.kt
package com.example.app_prueba.data.remote

import com.example.app_prueba.data.model.AuthResponse
import com.example.app_prueba.data.model.LoginRequest
import com.example.app_prueba.data.model.Product // Asegúrate de que esté importado
import com.example.app_prueba.data.model.ProductDetailResponse
import com.example.app_prueba.data.model.ProductListResponse
import com.example.app_prueba.data.model.Pokemon // <-- 1. IMPORTA EL NUEVO MODELO
import com.example.app_prueba.data.model.UserRegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // --- Tus rutas ---
    @POST("register")
    suspend fun registerUser(@Body user: UserRegisterRequest): Response<AuthResponse>

    @POST("login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<AuthResponse>

    @GET("products")
    suspend fun listProducts(
        @Query("q") q: String? = null,
        @Query("category_id") categoryId: Int? = null
    ): Response<ProductListResponse>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Response<ProductDetailResponse>

    // --- 2. AÑADE ESTA FUNCIÓN PARA LA API EXTERNA ---
    @GET("https://pokeapi.co/api/v2/pokemon/ditto")
    suspend fun getDitto(): Response<Pokemon>
}