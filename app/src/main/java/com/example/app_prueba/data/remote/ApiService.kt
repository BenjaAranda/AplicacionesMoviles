package com.example.app_prueba.data.remote

import com.example.app_prueba.data.model.* // Importa todos los modelos nuevos
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @GET("products")
    suspend fun listProducts(): Response<ProductListResponse>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Response<ProductResponse>

    // --- CARRITO ---
    @GET("cart")
    suspend fun getCart(@Header("Authorization") token: String): Response<CartListResponse>

    @POST("cart")
    suspend fun addToCart(
        @Header("Authorization") token: String,
        @Body request: AddToCartRequest
    ): Response<SimpleResponse>

    @PUT("cart/item/{itemId}")
    suspend fun updateCartQuantity(
        @Header("Authorization") token: String,
        @Path("itemId") itemId: Int,
        @Body request: UpdateCartRequest
    ): Response<SimpleResponse>

    @DELETE("cart/{itemId}")
    suspend fun deleteCartItem(
        @Header("Authorization") token: String,
        @Path("itemId") itemId: Int
    ): Response<SimpleResponse>

    @DELETE("cart")
    suspend fun clearCart(
        @Header("Authorization") token: String
    ): Response<SimpleResponse>

    // --- ORDENES ---
    @POST("orders")
    suspend fun createOrder(@Header("Authorization") token: String): Response<SimpleResponse>
    // --- SISTEMA DE PUNTOS  ---

    @GET("points/summary")
    suspend fun getPointsSummary(): Response<UserPointsSummary>

    @GET("points/history")
    suspend fun getPointsHistory(
        @Query("filter") filter: String
    ): Response<List<HistoryItem>>

    @GET("points/benefits")
    suspend fun getBenefits(): Response<List<BenefitItem>>

    @GET("points/rewards")
    suspend fun getRewards(): Response<List<RewardItem>>

    @POST("points/redeem/{rewardId}")
    suspend fun redeemReward(@Path("rewardId") rewardId: String): Response<Unit>
}