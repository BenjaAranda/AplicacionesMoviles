package com.example.app_prueba.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.app_prueba.data.model.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Upsert
    suspend fun upsertItem(item: CartItem)

    @Delete
    suspend fun deleteItem(item: CartItem)

    @Query("SELECT * FROM cart_items")
    fun getAllItems(): Flow<List<CartItem>>

    @Query("SELECT * FROM cart_items WHERE productCode = :productCode LIMIT 1")
    suspend fun getItemByCode(productCode: String): CartItem?

    @Query("DELETE FROM cart_items")
    suspend fun clearAllItems() // Nueva función para vaciar el carrito
}