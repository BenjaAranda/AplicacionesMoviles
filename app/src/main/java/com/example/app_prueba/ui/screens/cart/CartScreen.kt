package com.example.app_prueba.ui.screens.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_prueba.data.model.CartItem
import com.example.app_prueba.ui.components.Footer
import com.example.app_prueba.viewmodel.CartViewModel
import com.example.app_prueba.viewmodel.SessionViewModel

fun formatCurrency(price: Double): String {
    val format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("es", "CL"))
    format.maximumFractionDigits = 0
    return format.format(price)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, vm: CartViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
                    }
                    Text("Mi Carrito", style = MaterialTheme.typography.headlineMedium)
                }

                if (!SessionViewModel.isLoggedIn) {
                    NotLoggedInMessage(navController)
                } else if (state.items.isEmpty()) {
                    EmptyCartMessage()
                }
            }
        }

        if (SessionViewModel.isLoggedIn && state.items.isNotEmpty()) {
            items(state.items) { item ->
                CartItemRow(
                    item = item,
                    onIncrease = { vm.increaseQuantity(item) },
                    onDecrease = { vm.decreaseQuantity(item) }
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = { vm.clearCart() },
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                    ) {
                        Text("Vaciar Carrito", color = MaterialTheme.colorScheme.error)
                    }
                }
                CartSummary(
                    subtotal = state.subtotal,
                    discount = state.discount,
                    total = state.total,
                    navController = navController
                )
            }
        }

        item {
            Footer(navController = navController)
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.productName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(
                text = formatCurrency(item.productPrice * item.quantity),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDecrease) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "Disminuir cantidad")
            }
            Text(text = "${item.quantity}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = onIncrease) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Aumentar cantidad")
            }
        }
    }
}

@Composable
fun CartSummary(subtotal: Double, discount: Double, total: Double, navController: NavController) { // <--- 1. Agrega NavController aquí
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Divider(modifier = Modifier.padding(bottom = 16.dp))
        SummaryRow("Subtotal:", formatCurrency(subtotal))
        if (discount > 0) {
            SummaryRow("Descuento (20%):", "- ${formatCurrency(discount)}", MaterialTheme.colorScheme.secondary)
        }
        Spacer(modifier = Modifier.height(8.dp))
        SummaryRow("Total:", formatCurrency(total), fontWeight = FontWeight.Bold, isTotal = true)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("checkout") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("IR A PAGAR", fontSize = 18.sp)
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String, color: Color = MaterialTheme.colorScheme.onBackground, fontWeight: FontWeight = FontWeight.Normal, isTotal: Boolean = false) {
    val style = if (isTotal) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.bodyLarge
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, style = style, fontWeight = fontWeight, color = color)
        Text(value, style = style, fontWeight = fontWeight, color = color)
    }
}

@Composable
fun NotLoggedInMessage(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Inicia sesión para ver tu carrito", style = MaterialTheme.typography.headlineSmall, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("login") }) {
            Text("Iniciar Sesión")
        }
    }
}

@Composable
fun EmptyCartMessage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 50.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("Tu carrito está vacío", style = MaterialTheme.typography.headlineSmall)
    }
}