package com.example.app_prueba.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ContactViewModel : ViewModel() {
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var comment by mutableStateOf("")
    var emailError by mutableStateOf<String?>(null)

    private val allowedDomains = listOf("@duoc.cl", "@profesor.duoc.cl", "@gmail.com")

    private fun validateEmail(): Boolean {
        if (email.isBlank() || allowedDomains.none { email.endsWith(it) }) {
            emailError = "Por favor, usa un correo válido (${allowedDomains.joinToString()})"
            return false
        }
        emailError = null
        return true
    }

    fun onSubmit(context: Context) {
        if (validateEmail() && name.isNotBlank() && comment.isNotBlank()) {
            // Lógica de envío (en un proyecto real, se enviaría a un servidor)
            Toast.makeText(context, "Mensaje enviado con éxito", Toast.LENGTH_SHORT).show()
            // Limpiar campos
            name = ""
            email = ""
            comment = ""
        } else if (name.isBlank() || comment.isBlank()) {
            Toast.makeText(context, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    fun onChatWithWhatsApp(context: Context) {
        try {
            val phoneNumber = "+56912345678" // Reemplaza con un número de teléfono real
            val message = "Hola, necesito ayuda con la app Level-Up Gamer."

            val intent = Intent(Intent.ACTION_VIEW)
            val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}"
            intent.data = Uri.parse(url)
            intent.setPackage("com.whatsapp")
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp no está instalado.", Toast.LENGTH_SHORT).show()
        }
    }
}