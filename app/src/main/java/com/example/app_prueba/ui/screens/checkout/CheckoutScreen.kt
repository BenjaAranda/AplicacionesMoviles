package com.example.app_prueba.ui.screens.checkout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_prueba.ui.screens.cart.formatCurrency
import com.example.app_prueba.viewmodel.CartViewModel
import com.example.app_prueba.viewmodel.CheckoutViewModel
import com.example.app_prueba.viewmodel.PaymentMethod

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    checkoutViewModel: CheckoutViewModel = viewModel()
) {
    val cartState by cartViewModel.uiState.collectAsState()
    val checkoutState by checkoutViewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finalizar Compra") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            // Botón de Pagar fijo abajo
            Button(
                onClick = {
                    checkoutViewModel.processPayment {
                        cartViewModel.clearCart() // Vaciamos el carrito
                        navController.navigate("home") { // Volvemos al inicio
                            popUpTo("home") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                enabled = !checkoutState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (checkoutState.isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("PAGAR ${formatCurrency(cartState.total)}")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            // Mensaje de Error si falta datos
            if (checkoutState.errorMessage != null) {
                Text(
                    text = checkoutState.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // SECCIÓN 1: Dirección de Envío
            SectionTitle(title = "Dirección de Envío", icon = Icons.Default.LocalShipping)
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = checkoutState.address,
                        onValueChange = { checkoutViewModel.updateAddress(it) },
                        label = { Text("Dirección (Calle y Número)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = checkoutState.city,
                        onValueChange = { checkoutViewModel.updateCity(it) },
                        label = { Text("Ciudad / Comuna") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = checkoutState.phoneNumber,
                        onValueChange = { checkoutViewModel.updatePhone(it) },
                        label = { Text("Teléfono de contacto") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // SECCIÓN 2: Método de Pago
            SectionTitle(title = "Método de Pago", icon = Icons.Default.CreditCard)
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Column {
                    PaymentOptionRow(
                        title = "Tarjeta de Crédito",
                        selected = checkoutState.paymentMethod == PaymentMethod.CREDIT_CARD,
                        onClick = { checkoutViewModel.updatePaymentMethod(PaymentMethod.CREDIT_CARD) }
                    )
                    PaymentOptionRow(
                        title = "Tarjeta de Débito",
                        selected = checkoutState.paymentMethod == PaymentMethod.DEBIT_CARD,
                        onClick = { checkoutViewModel.updatePaymentMethod(PaymentMethod.DEBIT_CARD) }
                    )
                    PaymentOptionRow(
                        title = "Efectivo / Transferencia",
                        selected = checkoutState.paymentMethod == PaymentMethod.CASH,
                        onClick = { checkoutViewModel.updatePaymentMethod(PaymentMethod.CASH) },
                        icon = Icons.Default.Money
                    )
                }
            }

            // SECCIÓN 3: Resumen del Pedido
            SectionTitle(title = "Resumen del Pedido", icon = Icons.Default.Home)
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 80.dp)) { // Padding extra para no chocar con el botón
                Column(modifier = Modifier.padding(16.dp)) {
                    cartState.items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${item.quantity}x ${item.productName}", style = MaterialTheme.typography.bodyMedium)
                            Text(formatCurrency(item.productPrice * item.quantity), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total a Pagar", fontWeight = FontWeight.Bold)
                        Text(formatCurrency(cartState.total), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(8.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PaymentOptionRow(title: String, selected: Boolean, onClick: () -> Unit, icon: ImageVector = Icons.Default.CreditCard) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(modifier = Modifier.width(16.dp))
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(16.dp))
        Text(title)
    }
}