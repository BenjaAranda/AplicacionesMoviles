package com.example.app_prueba.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.example.app_prueba.navigation.Routes

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    fun onLogout(navController: NavController) {
        SessionViewModel.onLogout()
        navController.navigate(Routes.Login.route) {
            popUpTo(0) {
                inclusive = true
            }
        }
    }
}