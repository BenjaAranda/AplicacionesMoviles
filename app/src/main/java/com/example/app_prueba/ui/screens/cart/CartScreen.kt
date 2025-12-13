package com.example.app_prueba.ui.screens.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background // <-- Faltaba este import
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha // <-- Faltaba este import para la transparencia
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_prueba.data.model.CartItem
import com.example.app_prueba.ui.components.Footer
import com.example.app_prueba.ui.screens.products.getProductImageByNameForGrid
import com.example.app_prueba.viewmodel.CartViewModel
import com.example.app_prueba.viewmodel.SessionViewModel
import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(price: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    format.maximumFractionDigits = 0
    return format.format(price)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, vm: CartViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()

    // Recargar al entrar para ver los items de la nube
    LaunchedEffect(Unit) {
        vm.loadCart()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mi Carrito", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        if (!SessionViewModel.isLoggedIn) {
                            NotLoggedInMessage(navController)
                        } else if (state.items.isEmpty()) {
                            EmptyCartMessage()
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

                    item { Footer(navController) }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    // Usamos la función de mapeo para mostrar la imagen correcta
    val imageRes = getProductImageByNameForGrid(item.productName)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = item.productName,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.productName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(
                text = formatCurrency(item.productPrice * item.quantity),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 14.sp
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDecrease, modifier = Modifier.size(32.dp)) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "Disminuir", modifier = Modifier.size(16.dp))
            }
            Text(
                text = "${item.quantity}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            IconButton(onClick = onIncrease, modifier = Modifier.size(32.dp)) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Aumentar", modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun CartSummary(subtotal: Double, discount: Double, total: Double, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        SummaryRow("Subtotal:", formatCurrency(subtotal))
        if (discount > 0) {
            SummaryRow("Descuento (20%):", "- ${formatCurrency(discount)}", MaterialTheme.colorScheme.secondary)
        }
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        SummaryRow("Total:", formatCurrency(total), fontWeight = FontWeight.Bold, isTotal = true)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("checkout") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("IR A PAGAR", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String, color: Color = MaterialTheme.colorScheme.onSurface, fontWeight: FontWeight = FontWeight.Normal, isTotal: Boolean = false) {
    val style = if (isTotal) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyLarge
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
        Text("Inicia sesión para ver tu carrito", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("login") }) {
            Text("Iniciar Sesión")
        }
    }
}

@Composable
fun EmptyCartMessage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Usamos el modificador estándar alpha ahora que está importado correctamente
        Icon(
            painter = painterResource(id = com.example.app_prueba.R.drawable.logo_main),
            contentDescription = null,
            modifier = Modifier.size(100.dp).alpha(0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Tu carrito está vacío", style = MaterialTheme.typography.headlineSmall)
        Text("¡Agrega productos para comenzar!", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
    }
}