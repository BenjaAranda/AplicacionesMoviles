// ruta: application/LevelUpGamerApp.kt
package com.example.app_prueba.application

import android.app.Application
import com.example.app_prueba.data.database.AppDatabase

class LevelUpGamerApp : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}