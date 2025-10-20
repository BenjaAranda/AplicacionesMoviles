package com.example.app_prueba.viewmodel

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import androidx.lifecycle.ViewModel
import com.example.app_prueba.R
import com.example.app_prueba.data.model.BlogPost
import java.text.SimpleDateFormat
import java.util.*

class BlogViewModel : ViewModel() {

    val posts: List<BlogPost>
    val featuredPost: BlogPost

    init {
        posts = loadBlogPosts()
        featuredPost = posts.first()
    }

    // --- NUEVA FUNCIÓN PARA COMPARTIR ---
    fun sharePost(context: Context, post: BlogPost) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "¡Mira esta noticia de Level-Up Gamer!\n\n*${post.title}*\n\n${post.url}")
            putExtra(Intent.EXTRA_TITLE, post.title)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    // --- NUEVA FUNCIÓN PARA AGENDAR EVENTOS ---
    fun scheduleEvent(context: Context, post: BlogPost) {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, post.title)
            putExtra(CalendarContract.Events.DESCRIPTION, "Evento de la comunidad Level-Up Gamer. Más detalles en: ${post.url}")

            // Simulación de fecha del evento (puedes ajustarla)
            val calendar = Calendar.getInstance()
            try {
                // Intenta interpretar la fecha del post
                val sdf = SimpleDateFormat("d MMMM yyyy", Locale("es", "ES"))
                calendar.time = sdf.parse(post.date) ?: Date()
            } catch (e: Exception) {
                // Si falla, usa la fecha actual
            }
            // Fija la hora del evento (ej. a las 18:00)
            calendar.set(Calendar.HOUR_OF_DAY, 18)
            calendar.set(Calendar.MINUTE, 0)

            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.timeInMillis)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calendar.timeInMillis + 60 * 60 * 1000) // Duración de 1 hora
        }
        context.startActivity(intent)
    }
    // ------------------------------------------

    private fun loadBlogPosts(): List<BlogPost> {
        return listOf(
            BlogPost(
                title = "Review: La nueva PlayStation 5 Pro",
                date = "3 Septiembre 2025",
                summary = "Te contamos todas las novedades de la PS5 Pro: rendimiento mejorado, gráficos de última generación y una experiencia inmersiva sin precedentes...",
                url = "https://www.ign.com/articles/ps5-pro-everything-we-know",
                imageRes = R.drawable.ps5pro
            ),
            BlogPost(
                title = "Top 5 Sillas Gamers 2025",
                date = "2 Septiembre 2025",
                summary = "Comodidad y ergonomía: te mostramos las mejores opciones de sillas gamers para largas sesiones.",
                url = "https://www.pcgamer.com/best-gaming-chair/",
                imageRes = R.drawable.sillas_blog
            ),
            BlogPost(
                title = "Accesorios imprescindibles para streamers",
                date = "1 Septiembre 2025",
                summary = "Micrófonos, luces, capturadoras y más: todo lo que necesitas para mejorar tu setup de streaming.",
                url = "https://www.tomsguide.com/us/best-streaming-gear,review-5399.html",
                imageRes = R.drawable.streamers_blog
            ),
            BlogPost(
                title = "eSports en Chile: los torneos más esperados",
                date = "28 Agosto 2025",
                summary = "Un repaso por los eventos de eSports que marcarán este semestre en el país.",
                url = "https://esports.as.com/latam/",
                imageRes = R.drawable.esports
            )
        )
    }
}