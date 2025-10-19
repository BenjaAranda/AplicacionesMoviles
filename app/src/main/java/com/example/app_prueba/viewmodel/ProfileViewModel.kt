package com.example.app_prueba.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.example.app_prueba.navigation.Routes

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    fun onLogout(navController: NavController) {
        navController.navigate(Routes.Login.route) {
            popUpTo(0) {
                inclusive = true
            }
        }
    }

    fun contactSupport(context: Context) {
        try {
            val phoneNumber = "+56912345678" // Reemplazar despues
            val message = "Hola, necesito ayuda con la app Level-Up Gamer."

            val intent = Intent(Intent.ACTION_VIEW)
            val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}"
            intent.data = Uri.parse(url)
            intent.setPackage("com.whatsapp") // Asegura que se abra en WhatsApp
            context.startActivity(intent)
        } catch (e: Exception) {
            // Esto ocurre si WhatsApp no está instalado
            Toast.makeText(context, "WhatsApp no está instalado.", Toast.LENGTH_SHORT).show()
        }
    }
}