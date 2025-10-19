package com.example.app_prueba.ui.screens.home

// Imports originales

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_prueba.data.model.Product
import com.example.app_prueba.navigation.Routes
import com.example.app_prueba.ui.components.Footer
import com.example.app_prueba.viewmodel.HomeViewModel
import com.example.app_prueba.viewmodel.ProductCategory
import java.text.NumberFormat
import java.util.Locale

// --- IMPORTS PARA EL CARRUSEL ---
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.painterResource
import com.example.app_prueba.R
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale

// --- IMPORTS PARA EL MENÚ (YA NO SE USAN DROPDOWN) ---
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
// -----------------------------------------------------------
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.annotation.DrawableRes // Para el ID de la imagen
import androidx.compose.foundation.lazy.items // Para la nueva lista de banners
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction

// Definición de los colores neón
val NeonGreen = Color(0xFF39FF14)
val NeonPurple = Color(0xFFB026FF)

// AdItem (imageUrl es Int)
data class AdItem(
    val title: String,
    val description: String,
    val imageUrl: Int,
    val routeOnClick: String
)

// --- NUEVO DATA CLASS PARA LOS BANNERS ---
data class PromoBannerItem(
    @DrawableRes val imageUrl: Int,
    val contentDescription: String,
    val routeOnClick: String
)
// ----------------------------------------
@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = viewModel()) {
    val uiState by homeViewModel.uiState.collectAsState()
    val context = LocalContext.current

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // LazyColumn es clave aquí. Cuando 'isExpanded' cambie en CategoriesSection,
        // LazyColumn redibujará y "empujará" los items de abajo.
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item { AppHeaderSearch(navController) }
            item { HeroBanner(navController) }
            item { CategoriesSection(uiState.categories, navController) }

            item { Spacer(modifier = Modifier.height(24.dp)) } // Un poco de espacio


            item { SectionTitle("PRODUCTOS DESTACADOS") }
            item { FeaturedBannersSection(navController) }
            item {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 160.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .height(((uiState.featuredProducts.size + 1) / 2 * 290).dp),
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

// --- FUNCIÓN HERO BANNER (Sin cambios) ---
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroBanner(navController: NavController) {
    val adItems = remember {
        listOf(
            // Asumiendo que tienes estas imágenes en res/drawable
            AdItem("", "", R.drawable.product, Routes.Products.route),
            AdItem("", "", R.drawable.product, Routes.Products.route),
            AdItem("", "", R.drawable.product, Routes.Products.route)
        )
    }

    val pagerState = rememberPagerState(pageCount = { adItems.size })

    LaunchedEffect(pagerState) {
        while (true) {
            delay(4000L)
            val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 32.dp),
        ) { page ->
            AdCarouselItem(
                item = adItems[page],
                navController = navController,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        PageIndicator(
            pageCount = adItems.size,
            currentPage = pagerState.currentPage
        )
    }
}

// --- FUNCIÓN AdCarouselItem (Sin cambios) ---
@Composable
fun AdCarouselItem(
    item: AdItem,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(200.dp)
            .clickable { navController.navigate(item.routeOnClick) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Image(
            painter = painterResource(id = item.imageUrl),
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

// --- FUNCIÓN PageIndicator (Sin cambios) ---
@Composable
fun PageIndicator(pageCount: Int, currentPage: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val color = if (index == currentPage) NeonGreen else Color.Gray
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}


// --- ¡FUNCIÓN CategoriesSection MODIFICADA! ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesSection(categories: List<ProductCategory>, navController: NavController) {

    // Estado para saber si el menú está abierto o cerrado
    var isExpanded by remember { mutableStateOf(false) }

    // Icono que cambia (flecha arriba/abajo)
    val icon = if (isExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            // Agregamos animación para que la apertura y cierre sea suave
            .animateContentSize()
    ) {
        SectionTitle("Categorías")

        // El "botón" (campo de texto) que el usuario clica
        OutlinedTextField(
            value = "Selecciona una categoría",
            onValueChange = { },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }, // Abre/cierra el menú al clicar
            trailingIcon = {
                Icon(icon, "Abrir/Cerrar menú", Modifier.clickable { isExpanded = !isExpanded })
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = NeonGreen,
                unfocusedIndicatorColor = Color.Gray,
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White
            )
        )

        if (isExpanded) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp), // Pequeño espacio
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .heightIn(max = 300.dp) // <-- NUEVO: Limita la altura máxima
                        .verticalScroll(rememberScrollState()) // <-- NUEVO: Hace que sea desplazable
                        .padding(vertical = 8.dp)
                ) {
                    categories.forEach { category ->
                        // Cada categoría es una fila clickeable
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // 1. Navega
                                    val routeWithCategory =
                                        "${Routes.Products.route}?category=${category.name}"
                                    navController.navigate(routeWithCategory)
                                    // 2. Cierra el menú
                                    isExpanded = false
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = category.name,
                                color = NeonPurple,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}


// --- RESTO DE TUS FUNCIONES ORIGINALES (SIN CAMBIOS) ---

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = NeonPurple,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 16.dp),
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeaderSearch(navController: NavController) { // <-- 1. Añadimos navController como parámetro
    var searchText by remember { mutableStateOf("") }
    // 2. Declaramos el controlador del teclado aquí
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text("Busca tus productos...", color = Color.Gray) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(0.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = NeonGreen,
                unfocusedIndicatorColor = Color.Gray,
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White
            ),
            maxLines = 1,
            singleLine = true,
            // --- Para que el botón "Buscar" del teclado funcione ---
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (searchText.isNotBlank()) {
                        navController.navigate("${Routes.Products.route}?search=${searchText}")
                        keyboardController?.hide() // Oculta el teclado
                    }
                }
            )
        )

        Button(
            onClick = {
                if (searchText.isNotBlank()) {
                    // Ahora sí 'navController' y 'keyboardController' existen
                    navController.navigate("${Routes.Products.route}?search=${searchText}")
                    keyboardController?.hide()
                }
            },
            modifier = Modifier.height(56.dp),
            shape = RoundedCornerShape(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
            contentPadding = PaddingValues(12.dp)
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = "Buscar",
                tint = Color.Black
            )
        }
    }
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
            Box(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("Imagen", fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = product.name, fontWeight = FontWeight.Bold, maxLines = 1, fontSize = 14.sp, color = NeonPurple)
            Text(text = "Categoría: ${product.category}", fontSize = 12.sp, color = Color.Gray)
            Text(text = formatCurrency(product.price), color = NeonPurple, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onAddToCartClick() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
            ) {
                Text("Agregar al carrito", fontSize = 12.sp, color = Color.Black)
            }
        }
    }
}
// --- COMPOSABLE PARA UN BANNER INDIVIDUAL ---
@Composable
fun PromoBanner(
    item: PromoBannerItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp) // Altura similar a la de la imagen
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp) // Bordes redondeados
    ) {
        Image(
            painter = painterResource(id = item.imageUrl),
            contentDescription = item.contentDescription,
            contentScale = ContentScale.Crop, // Para que la imagen llene el espacio
            modifier = Modifier.fillMaxSize()
        )
    }
}

// --- SECCIÓN QUE CONTIENE LA LISTA DE BANNERS ---
@Composable
fun FeaturedBannersSection(navController: NavController) {
    // Lista de ejemplo con tus banners. Deberás reemplazar las imágenes y rutas.
    val promoBanners = remember {
        listOf(
            PromoBannerItem(
                imageUrl = R.drawable.product, // Reemplaza con tu imagen
                contentDescription = "Promoción de Halloween",
                routeOnClick = Routes.Products.route
            ),
            PromoBannerItem(
                imageUrl = R.drawable.product, // Reemplaza con tu imagen
                contentDescription = "Libros con envío rápido",
                routeOnClick = Routes.Products.route
            ),
            PromoBannerItem(
                imageUrl = R.drawable.product, // Reemplaza con tu imagen
                contentDescription = "Libros importados",
                routeOnClick = Routes.Products.route
            )
        )
    }

    // Usamos LazyColumn para mostrar la lista de banners verticalmente.
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 420.dp), // Limita la altura si hay muchos banners
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre cada banner
    ) {
        items(promoBanners) { banner ->
            PromoBanner(
                item = banner,
                onClick = { navController.navigate(banner.routeOnClick) }
            )
        }
    }
}



fun formatCurrency(price: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    format.maximumFractionDigits = 0
    return "CLP$${format.format(price).replace("CLP", "").trim()}"
}