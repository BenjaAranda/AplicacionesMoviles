// ruta: data/model/Pokemon.kt
package com.example.app_prueba.data.model

import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String
)