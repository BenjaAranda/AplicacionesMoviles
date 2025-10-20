package com.example.app_prueba.data.model

import androidx.annotation.DrawableRes

// Clase que define la estructura de un miembro del equipo
data class TeamMember(
    val name: String,
    val role: String,
    @DrawableRes val imageRes: Int // Recurso de la imagen en la carpeta drawable
)