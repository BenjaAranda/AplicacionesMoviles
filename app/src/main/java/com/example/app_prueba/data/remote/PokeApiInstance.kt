package com.example.app_prueba.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

// 1. Interfaz de la PokeAPI
interface PokeApiService {
    @GET("pokemon/{name}")
    suspend fun getPokemon(@Path("name") name: String): retrofit2.Response<PokemonResponse>
}

// 2. Modelo de respuesta mínimo (Nombre y Sprites)
data class PokemonResponse(
    val name: String,
    val sprites: PokemonSprites
)

data class PokemonSprites(
    val front_default: String?
)

// 3. Instancia Singleton
object PokeApiInstance {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    val api: PokeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApiService::class.java)
    }
}