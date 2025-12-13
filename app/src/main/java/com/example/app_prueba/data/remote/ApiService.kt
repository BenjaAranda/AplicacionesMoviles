package com.example.app_prueba.data.remote

import com.example.app_prueba.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // --- USUARIOS ---
    @POST("register")
    suspend fun registerUser(@Body user: UserRegisterRequest): Response<AuthResponse>

    @POST("login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<AuthResponse>

    // --- PRODUCTOS ---
    @GET("products")
    suspend fun listProducts(
        @Query("q") q: String? = null,
        @Query("category_id") categoryId: Int? = null
    ): Response<ProductListResponse>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Response<ProductDetailResponse>

    // --- API EXTERNA ---
    @GET("https://pokeapi.co/api/v2/pokemon/ditto")
    suspend fun getDitto(): Response<Pokemon>

    // --- CARRITO (NUEVO) ---
    // El backend espera el token en el Header (Authorization: Bearer <token>)
    // Retrofit puede manejar esto, o lo pasamos manual. Por ahora manual es más fácil de entender.

    @GET("cart")
    suspend fun getCart(
        @Header("Authorization") token: String
    ): Response<CartListResponse> // Necesitaremos crear este modelo abajo

    @POST("cart")
    suspend fun addToCart(
        @Header("Authorization") token: String,
        @Body request: AddToCartRequest // Necesitaremos crear este modelo
    ): Response<SimpleResponse> // Respuesta genérica

    // --- ÓRDENES (NUEVO) ---
    @POST("orders")
    suspend fun createOrder(
        @Header("Authorization") token: String
    ): Response<OrderResponse> // Necesitaremos crear este modelo

    // 1. Obtener resumen de puntos del usuario
    @GET("points/summary")
    suspend fun getPointsSummary(): Response<UserPointsSummary>

    // 2. Obtener historial (con filtro opcional)
    @GET("points/history")
    suspend fun getPointsHistory(
        @Query("filter") filter: String
    ): Response<List<HistoryItem>>

    // 3. Obtener beneficios activos
    @GET("points/benefits")
    suspend fun getBenefits(): Response<List<BenefitItem>>

    // 4. Obtener recompensas disponibles
    @GET("points/rewards")
    suspend fun getRewards(): Response<List<RewardItem>>

    // 5. Canjear una recompensa
    @POST("points/redeem/{rewardId}")
    suspend fun redeemReward(@Path("rewardId") rewardId: String): Response<Unit>
}