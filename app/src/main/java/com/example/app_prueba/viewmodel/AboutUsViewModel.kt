package com.example.app_prueba.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app_prueba.R
import com.example.app_prueba.data.model.TeamMember

class AboutUsViewModel : ViewModel() {

    // Lista de miembros del equipo
    val teamMembers: List<TeamMember> = listOf(
        TeamMember(
            name = "Benjamín Aranda",
            role = "Desarrollador Fullstack",
            imageRes = R.drawable.gato // Reemplaza 'gato' con el nombre real de la imagen si lo tienes
        ),
        TeamMember(
            name = "Joaquín Robles",
            role = "Desarrollador Fullstack",
            imageRes = R.drawable.gato // Reemplaza 'gato' con el nombre real de la imagen si lo tienes
        ),
        TeamMember(
            name = "########",
            role = "Soporte Técnico",
            imageRes = R.drawable.gato // Reemplaza 'gato' con el nombre real de la imagen si lo tienes
        )
    )

    // Lista de valores de la empresa
    val companyValues: List<String> = listOf(
        "Pasión por lo gamer",
        "Calidad garantizada",
        "Cercanía con nuestros clientes",
        "Compromiso con la comunidad"
    )
}