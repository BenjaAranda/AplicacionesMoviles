package com.example.app_prueba.data.remote

import com.example.app_prueba.data.model.AuthResponse
import com.example.app_prueba.data.model.LoginRequest
import com.example.app_prueba.data.model.Product // Asegúrate de que Product esté importado
import com.example.app_prueba.data.model.ProductDetailResponse // Importa la nueva clase
import com.example.app_prueba.data.model.ProductListResponse // Importa la nueva clase
import com.example.app_prueba.data.model.UserRegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    /**
     * Registra un nuevo usuario.
     * Ruta corregida: "register"
     */
    @POST("register")
    suspend fun registerUser(@Body user: UserRegisterRequest): Response<AuthResponse>

    /**
     * Inicia sesión de un usuario.
     * Ruta corregida: "login"
     */
    @POST("login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<AuthResponse>

    /**
     * Obtiene la lista de productos.
     * Ruta corregida: "products"
     * Tipo de respuesta corregido: ProductListResponse
     */
    @GET("products")
    suspend fun listProducts(
        @Query("q") q: String? = null,
        @Query("category_id") categoryId: Int? = null
    ): Response<ProductListResponse> // <-- CORREGIDO

    /**
     * Obtiene un producto específico por su ID.
     * Ruta corregida: "products/{id}"
     * Tipo de respuesta corregido: ProductDetailResponse
     */
    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Response<ProductDetailResponse> // <-- CORREGIDO
}