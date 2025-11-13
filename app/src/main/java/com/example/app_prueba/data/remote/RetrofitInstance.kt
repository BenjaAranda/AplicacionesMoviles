package com.example.app_prueba.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // 10.0.2.2 es la IP especial que usa el emulador de Android
    // para conectarse al "localhost" de tu computador.
    // El puerto 5000 es donde está corriendo tu API de Flask.
    private const val BASE_URL = "http://10.0.2.2:5000/api/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}