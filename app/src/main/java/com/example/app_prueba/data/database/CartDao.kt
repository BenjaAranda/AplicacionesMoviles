package com.example.app_prueba.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.app_prueba.data.model.CartItem

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    suspend fun getAllItems(): List<CartItem>

    @Query("SELECT * FROM cart_items WHERE productCode = :code LIMIT 1")
    suspend fun getItemByCode(code: String): CartItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertItem(item: CartItem)

    @Delete
    suspend fun deleteItem(item: CartItem)

    // --- LA FUNCIÓN QUE FALTABA ---
    @Query("DELETE FROM cart_items")
    suspend fun deleteAll()
}