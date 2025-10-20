package com.example.app_prueba.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app_prueba.R
import com.example.app_prueba.data.model.BlogPost

class BlogViewModel : ViewModel() {

    // Lista de todas las publicaciones del blog
    val posts: List<BlogPost>

    // La primera publicación de la lista será la destacada
    val featuredPost: BlogPost

    init {
        posts = loadBlogPosts()
        featuredPost = posts.first()
    }

    private fun loadBlogPosts(): List<BlogPost> {
        return listOf(
            BlogPost(
                title = "Review: La nueva PlayStation 5 Pro",
                date = "3 Septiembre 2025",
                summary = "Te contamos todas las novedades de la PS5 Pro: rendimiento mejorado, gráficos de última generación y una experiencia inmersiva sin precedentes...",
                url = "https://www.ign.com/articles/ps5-pro-everything-we-know", // URL de ejemplo
                imageRes = R.drawable.ps5pro
            ),
            BlogPost(
                title = "Top 5 Sillas Gamers 2025",
                date = "2 Septiembre 2025",
                summary = "Comodidad y ergonomía: te mostramos las mejores opciones de sillas gamers para largas sesiones.",
                url = "https://www.pcgamer.com/best-gaming-chair/", // URL de ejemplo
                imageRes = R.drawable.sillas_blog
            ),
            BlogPost(
                title = "Accesorios imprescindibles para streamers",
                date = "1 Septiembre 2025",
                summary = "Micrófonos, luces, capturadoras y más: todo lo que necesitas para mejorar tu setup de streaming.",
                url = "https://www.tomsguide.com/us/best-streaming-gear,review-5399.html", // URL de ejemplo
                imageRes = R.drawable.streamers_blog
            ),
            BlogPost(
                title = "eSports en Chile: los torneos más esperados",
                date = "28 Agosto 2025",
                summary = "Un repaso por los eventos de eSports que marcarán este semestre en el país.",
                url = "https://esports.as.com/latam/", // URL de ejemplo
                imageRes = R.drawable.esports
            )
        )
    }
}