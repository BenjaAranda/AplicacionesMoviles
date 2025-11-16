package com.example.app_prueba.data.model

import androidx.room.Entity
import androidx.room.Index // <-- Asegúrate de importar esto
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// --- INICIO DE CORRECCIÓN ---
@Entity(
    tableName = "users",
    // Agrega esto para que Room sepa que el email no se puede repetir
    indices = [Index(value = ["email"], unique = true)]
)
// --- FIN DE CORRECCIÓN ---
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String?,

    val email: String,

    @SerializedName("pass")
    val pass: String,

    @SerializedName("has_duoc_discount")
    val hasDuocDiscount: Boolean = false
)