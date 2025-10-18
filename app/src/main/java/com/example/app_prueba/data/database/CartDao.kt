package com.example.app_prueba.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.app_prueba.data.model.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Upsert // Inserta o actualiza si ya existe
    suspend fun upsertItem(item: CartItem)

    @Delete
    suspend fun deleteItem(item: CartItem)

    @Query("SELECT * FROM cart_items")
    fun getAllItems(): Flow<List<CartItem>>

    @Query("SELECT * FROM cart_items WHERE productCode = :productCode LIMIT 1")
    suspend fun getItemByCode(productCode: String): CartItem?
}