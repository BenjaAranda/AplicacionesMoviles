package com.example.app_prueba.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.app_prueba.data.model.User

@Dao
interface UserDao {

    // --- INICIO DE CORRECCIÓN ---
    // Cambia ABORT por REPLACE
    // Esto actualizará al usuario si ya existe, en lugar de fallar o duplicarlo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)
    // --- FIN DE CORRECCIÓN ---

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?
}