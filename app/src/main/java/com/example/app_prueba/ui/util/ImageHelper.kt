package com.example.app_prueba.ui.util

import androidx.compose.runtime.Composable
import com.example.app_prueba.R

/**
 * Asocia el código de un producto con su recurso de imagen en la carpeta `drawable`.
 * Los nombres de las imágenes deben coincidir con los definidos aquí.
 *
 * @param productCode El código del producto (ej. "JM001").
 * @return El ID del recurso de la imagen correspondiente.
 */
@Composable
fun getProductImage(productCode: String): Int {
    return when (productCode.uppercase()) {
        // Juegos de Mesa
        "JM001" -> R.drawable.catan
        "JM002" -> R.drawable.carcassonne

        // Accesorios
        "AC001" -> R.drawable.control_xbox
        "AC002" -> R.drawable.auriculares_hyperxcloud2

        // Consolas
        "CO001" -> R.drawable.playstation5

        // Computadores Gamers
        "CG001" -> R.drawable.pcgamer_asus

        // Sillas Gamers
        "SG001" -> R.drawable.silla_gamer_titan

        // Mouse
        "MS001" -> R.drawable.mouse_logitech

        // Mousepad
        "MP001" -> R.drawable.mousepad_razer

        // Poleras
        "PP001" -> R.drawable.poleragamer_personalizada

        // Imagen por defecto si no se encuentra una coincidencia.
        else -> R.drawable.product // Usamos 'product.jpeg' como placeholder
    }
}