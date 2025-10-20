package com.example.app_prueba.data.model

import androidx.annotation.DrawableRes

// Clase que define la estructura de una publicación de blog
data class BlogPost(
    val title: String,
    val date: String,
    val summary: String,
    val url: String, // URL a la noticia completa
    @DrawableRes val imageRes: Int // Recurso de la imagen en la carpeta drawable
)