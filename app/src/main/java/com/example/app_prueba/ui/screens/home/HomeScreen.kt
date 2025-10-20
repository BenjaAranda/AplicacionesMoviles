package com.example.app_prueba.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_prueba.R
import com.example.app_prueba.data.model.Product
import com.example.app_prueba.navigation.Routes
import com.example.app_prueba.ui.components.Footer
import com.example.app_prueba.ui.util.getProductImage
import com.example.app_prueba.viewmodel.HomeViewModel
import com.example.app_prueba.viewmodel.ProductCategory
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = viewModel()) {
    val uiState by homeViewModel.uiState.collectAsState()
    val context = LocalContext.current

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item { HeroBanner(navController) }
            item { CategoriesSection(uiState.categories, navController) }
            item { SectionTitle("PRODUCTOS DESTACADOS") }
            item {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 160.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .height( ( (uiState.featuredProducts.size + 1) / 2 * 290 ).dp ),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    userScrollEnabled = false
                ) {
                    items(uiState.featuredProducts) { product ->
                        ProductCard(
                            product = product,
                            onCardClick = {
                                navController.navigate(Routes.ProductDetail.createRoute(product.code))
                            },
                            onAddToCartClick = {
                                homeViewModel.addToCart(product)
                                Toast.makeText(context, "${product.name} añadido", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
            item { Footer(navController) }
        }
    }
}

@Composable
fun HeroBanner(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.banner),
                contentDescription = "Banner principal",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Equipamiento Gamer",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Consolas, PCs, Sillas y más",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate(Routes.Products.route) }) {
                    Text("¡Explorar ahora!")
                }
            }
        }
    }
}

@Composable
fun CategoriesSection(categories: List<ProductCategory>, navController: NavController) {
    Column {
        SectionTitle("Categorías")
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { navController.navigate(Routes.Products.route) }
                ) {
                    Image(
                        painter = painterResource(id = category.imageRes),
                        contentDescription = category.name,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = category.name,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 16.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun ProductCard(product: Product, onCardClick: () -> Unit, onAddToCartClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = painterResource(id = getProductImage(product.code)),
                contentDescription = product.name,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp)),
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
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                Text("Agregar al carrito", fontSize = 12.sp)
            }
        }
    }
}

fun formatCurrency(price: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    format.maximumFractionDigits = 0
    return "CLP$${format.format(price).trim()}"
}