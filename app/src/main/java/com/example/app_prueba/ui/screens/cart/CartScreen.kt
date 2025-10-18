package com.example.app_prueba.ui.screens.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_prueba.data.model.CartItem
import com.example.app_prueba.viewmodel.CartViewModel

// Función para formatear la moneda
fun formatCurrency(price: Double): String {
    val format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("es", "CL"))
    format.maximumFractionDigits = 0
    return format.format(price)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, vm: CartViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Mi Carrito") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío", style = MaterialTheme.typography.headlineSmall)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(state.items) { item ->
                        CartItemRow(item = item, onDelete = { vm.deleteItem(item) })
                    }
                }
                Divider()
                CartSummary(total = state.total)
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, onDelete: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.productName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = "Cantidad: ${item.quantity}", fontSize = 16.sp)
            Text(text = formatCurrency(item.productPrice * item.quantity), color = MaterialTheme.colorScheme.secondary)
        }
        IconButton(onClick = onDelete) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar item")
        }
    }
}

@Composable
fun CartSummary(total: Double) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Total:", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(formatCurrency(total), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.secondary)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* Lógica para pagar irá aquí */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("PAGAR", fontSize = 18.sp)
        }
    }
}