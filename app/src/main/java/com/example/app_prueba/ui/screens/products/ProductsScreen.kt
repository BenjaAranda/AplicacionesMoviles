package com.example.app_prueba.ui.screens.products

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_prueba.data.model.Product
import com.example.app_prueba.navigation.Routes
import com.example.app_prueba.ui.components.Footer
import com.example.app_prueba.ui.util.getProductImage
import com.example.app_prueba.viewmodel.ProductsViewModel
import com.example.app_prueba.viewmodel.SortOption
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductsScreen(navController: NavController, vm: ProductsViewModel = viewModel()) {
    val uiState by vm.uiState.collectAsState()
    val context = LocalContext.current

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            FiltersPanel(
                state = uiState,
                onSearchQueryChange = { vm.onSearchQueryChange(it) },
                onCategorySelected = { vm.onCategorySelected(it) },
                onMinPriceChange = { vm.onMinPriceChange(it) },
                onMaxPriceChange = { vm.onMaxPriceChange(it) },
                onSortOptionChange = { vm.onSortOptionChange(it) },
                onApplyFilters = { vm.applyFilters() }
            )
        }

        if (uiState.isLoading) {
            item {
                Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else if (uiState.products.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillParentMaxSize().padding(top = 100.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text("No se encontraron productos con esos filtros.")
                }
            }
        } else {
            item {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 160.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(( (uiState.products.size + 1) / 2 * 300 ).dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    userScrollEnabled = false
                ) {
                    items(uiState.products) { product ->
                        ProductGridItem(
                            product = product,
                            navController = navController,
                            onAddToCartClick = {
                                vm.addToCart(product)
                                Toast.makeText(context, "${product.name} añadido", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }

        item {
            Footer(navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersPanel(
    state: com.example.app_prueba.viewmodel.ProductsState,
    onSearchQueryChange: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onMinPriceChange: (String) -> Unit,
    onMaxPriceChange: (String) -> Unit,
    onSortOptionChange: (SortOption) -> Unit,
    onApplyFilters: () -> Unit
) {
    var sortMenuExpanded by remember { mutableStateOf(false) }
    var categoryMenuExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Buscar producto...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Filtros", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ExposedDropdownMenuBox(modifier = Modifier.weight(1f), expanded = sortMenuExpanded, onExpandedChange = { sortMenuExpanded = !sortMenuExpanded }) {
                OutlinedTextField(
                    value = when(state.sortOption) {
                        SortOption.PRICE_ASC -> "Precio: Menor a Mayor"
                        SortOption.PRICE_DESC -> "Precio: Mayor a Menor"
                        SortOption.NAME_ASC -> "Nombre (A-Z)"
                        else -> "Por defecto"
                    },
                    onValueChange = {}, readOnly = true, label = { Text("Ordenar") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sortMenuExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(expanded = sortMenuExpanded, onDismissRequest = { sortMenuExpanded = false }) {
                    DropdownMenuItem(text = { Text("Por defecto") }, onClick = { onSortOptionChange(SortOption.DEFAULT); sortMenuExpanded = false })
                    DropdownMenuItem(text = { Text("Precio: Menor a Mayor") }, onClick = { onSortOptionChange(SortOption.PRICE_ASC); sortMenuExpanded = false })
                    DropdownMenuItem(text = { Text("Precio: Mayor a Menor") }, onClick = { onSortOptionChange(SortOption.PRICE_DESC); sortMenuExpanded = false })
                    DropdownMenuItem(text = { Text("Nombre (A-Z)") }, onClick = { onSortOptionChange(SortOption.NAME_ASC); sortMenuExpanded = false })
                }
            }
            ExposedDropdownMenuBox(modifier = Modifier.weight(1f), expanded = categoryMenuExpanded, onExpandedChange = { categoryMenuExpanded = !categoryMenuExpanded }) {
                OutlinedTextField(
                    value = state.selectedCategory,
                    onValueChange = {}, readOnly = true, label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryMenuExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(expanded = categoryMenuExpanded, onDismissRequest = { categoryMenuExpanded = false }) {
                    state.categories.forEach { category ->
                        DropdownMenuItem(text = { Text(category) }, onClick = { onCategorySelected(category); categoryMenuExpanded = false })
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = state.minPrice, onValueChange = onMinPriceChange, label = { Text("Precio mín.") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = state.maxPrice, onValueChange = onMaxPriceChange, label = { Text("Precio máx.") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onApplyFilters, modifier = Modifier.fillMaxWidth()) {
            Text("Filtrar")
        }
    }
}

@Composable
fun ProductGridItem(product: Product, navController: NavController, onAddToCartClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = painterResource(id = getProductImage(product.code)),
                contentDescription = product.name,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { navController.navigate(Routes.ProductDetail.createRoute(product.code)) },
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp
            )
            Text("Categoría: ${product.category}", fontSize = 12.sp, color = Color.Gray)
            Text(formatCurrency(product.price), color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onAddToCartClick() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                Text("Agregar al carrito", fontSize = 12.sp)
            }
            OutlinedButton(
                onClick = { navController.navigate(Routes.ProductDetail.createRoute(product.code)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                contentPadding = PaddingValues(vertical = 4.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Text("Ver detalle", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

fun formatCurrency(price: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    format.maximumFractionDigits = 0
    return "CLP$${format.format(price).trim()}"
}