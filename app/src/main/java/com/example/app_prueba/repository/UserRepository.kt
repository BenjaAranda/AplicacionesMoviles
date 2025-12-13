package com.example.app_prueba.repository

import com.example.app_prueba.data.model.* // Modelos de tu App (Login, Register)
import com.example.app_prueba.data.remote.RetrofitInstance // Tu Backend
import com.example.app_prueba.data.remote.PokeApiInstance // PokeAPI (Restaurado)
import retrofit2.Response

class UserRepository {
    // API de tu Backend (Flask)
    private val api = RetrofitInstance.api
    // API de Pokémon
    private val pokeApi = PokeApiInstance.api

    // --- TUS FUNCIONES DE BACKEND ---
    suspend fun loginUser(email: String, pass: String): Response<LoginResponse> {
        return api.login(LoginRequest(email, pass))
    }

    suspend fun registerUser(email: String, pass: String, name: String, hasDuoc: Boolean): Response<RegisterResponse> {
        return api.register(RegisterRequest(email, pass, name, hasDuoc))
    }

    // --- FUNCIÓN RESTAURADA: POKEAPI ---
    suspend fun getDitto() = pokeApi.getPokemon("ditto")
}