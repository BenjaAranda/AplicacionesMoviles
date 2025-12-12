package com.example.app_prueba.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String = "",

    @SerializedName("price")
    val price: Double,

    @SerializedName("stock")
    val stock: Int = 0,

    @SerializedName("category_name") // El backend envía "category_name"
    val category: String,

    @SerializedName("image_url")
    val imageUrl: String? = null,

    // Campo auxiliar para tu lógica local (código antiguo),
    // lo mapeamos al ID por ahora para que no rompa tu UI
    val code: String = ""
) {
    // Un pequeño truco para que tu UI vieja que usa 'code' siga funcionando
    // usando el ID del backend como código.
    fun getEffectiveCode(): String {
        return if (code.isNotEmpty()) code else id.toString()
    }
}