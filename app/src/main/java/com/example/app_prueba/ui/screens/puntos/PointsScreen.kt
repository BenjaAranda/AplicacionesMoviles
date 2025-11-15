package com.example.app_prueba.ui.screens.puntos

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
// Importa las clases de estado desde el ViewModel
import com.example.app_prueba.viewmodel.BenefitItem
import com.example.app_prueba.viewmodel.HistoryItem
import com.example.app_prueba.viewmodel.PointUiState
import com.example.app_prueba.viewmodel.PointViewModel
import com.example.app_prueba.viewmodel.RewardItem

// --- Colores (pueden moverse a un archivo Theme.kt) ---
val DarkBackground = Color(0xFF000000)
val NeonPurple = Color(0xFF9D00FF)
val NeonGreen = Color(0xFF39FF14)
val TextColor = Color.White
val CardBackground = Color(0xFF1A1A1A)
val SubtleGray = Color(0xFF333333)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuntosScreen(
    navController: NavController,
    viewModel: PointViewModel = viewModel() // Inyecta el ViewModel
) {
    // Observa el estado del ViewModel
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { Text("Mis Puntos", color = TextColor) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = TextColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Pasa el estado a los componentes hijos
            ResumenDePuntos(uiState = uiState)

            Divider(color = SubtleGray, thickness = 1.dp)

            ReglasDeAcumulacion(rules = uiState.rules)

            Divider(color = SubtleGray, thickness = 1.dp)

            HistorialDePuntos(
                selectedFilter = uiState.selectedHistoryFilter,
                filters = uiState.historyFilters,
                historyItems = uiState.historyItems,
                onFilterSelected = { filter ->
                    // Notifica el evento al ViewModel
                    viewModel.onFilterSelected(filter)
                }
            )

            Divider(color = SubtleGray, thickness = 1.dp)

            Beneficios(benefits = uiState.benefits)

            Divider(color = SubtleGray, thickness = 1.dp)

            TiendaDeRecompensas(
                rewards = uiState.rewards,
                onRedeem = { rewardId ->
                    // Notifica el evento al ViewModel
                    viewModel.onRedeemReward(rewardId)
                }
            )

            Spacer(modifier = Modifier.height(16.dp)) // Espacio al final
        }
    }
}

// --- COMPONENTES DE UI (Stateless) ---

@Composable
fun ResumenDePuntos(uiState: PointUiState) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Resumen de Puntos",
            style = MaterialTheme.typography.headlineSmall,
            color = TextColor
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Nivel actual: ${uiState.currentLevel}", style = MaterialTheme.typography.bodyMedium, color = TextColor)
        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { uiState.progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp),
            color = NeonPurple,
            trackColor = SubtleGray
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Puntos acumulados este año: ${uiState.pointsThisYear}", style = MaterialTheme.typography.bodyMedium, color = TextColor)
        Text("Puntos disponibles: ${uiState.pointsAvailable}", style = MaterialTheme.typography.bodyMedium, color = TextColor)
        Text(
            "Puntos por caducar: ${uiState.pointsToCaducate}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Red
        )
    }
}

@Composable
fun ReglasDeAcumulacion(rules: List<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Reglas de Acumulación",
            style = MaterialTheme.typography.headlineSmall,
            color = TextColor
        )
        Spacer(modifier = Modifier.height(16.dp))

        rules.forEach { rule ->
            Text("• $rule", style = MaterialTheme.typography.bodyMedium, color = TextColor)
        }
    }
}

@Composable
fun HistorialDePuntos(
    selectedFilter: String,
    filters: List<String>,
    historyItems: List<HistoryItem>,
    onFilterSelected: (String) -> Unit // Recibe la lambda del evento
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Historial de Puntos",
            style = MaterialTheme.typography.headlineSmall,
            color = TextColor
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filters) { filter ->
                val isSelected = filter == selectedFilter
                Button(
                    onClick = { onFilterSelected(filter) }, // Llama al evento
                    colors = ButtonDefaults.buttonColors(
                        // Color verde si está seleccionado
                        containerColor = if (isSelected) NeonGreen else NeonPurple,
                        contentColor = if (isSelected) DarkBackground else TextColor
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Text(filter, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Encabezado de la tabla
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Fecha", modifier = Modifier.weight(1.5f), color = Color.Gray, style = MaterialTheme.typography.labelMedium)
            Text("Evento", modifier = Modifier.weight(2f), color = Color.Gray, style = MaterialTheme.typography.labelMedium)
            Text("Puntos", modifier = Modifier.weight(1f), color = Color.Gray, style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.End)
            Text("Expira", modifier = Modifier.weight(1.5f), color = Color.Gray, style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.End)
        }
        Divider(color = SubtleGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

        // Filas (vienen del estado)
        if (historyItems.isEmpty()) {
            Text(
                "No hay movimientos para este filtro.",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                textAlign = TextAlign.Center
            )
        } else {
            historyItems.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(item.date, modifier = Modifier.weight(1.5f), color = TextColor, style = MaterialTheme.typography.bodySmall)
                    Text(item.event, modifier = Modifier.weight(2f), color = TextColor, style = MaterialTheme.typography.bodySmall)
                    Text(
                        item.points,
                        modifier = Modifier.weight(1f),
                        // Color verde si es ganancia, rojo si es pérdida
                        color = if (item.isGain) NeonGreen else Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.End
                    )
                    Text(item.expires, modifier = Modifier.weight(1.5f), color = TextColor, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.End)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Beneficios(benefits: List<BenefitItem>) {
    val pagerState = rememberPagerState(pageCount = { benefits.size })

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Slider
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 32.dp)
        ) { page ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(CardBackground, RoundedCornerShape(8.dp))
                    .padding(16.dp)
                    .height(120.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(benefits[page].title, style = MaterialTheme.typography.titleMedium, color = NeonPurple)
                Spacer(modifier = Modifier.height(8.dp))
                Text(benefits[page].description, style = MaterialTheme.typography.bodyMedium, color = TextColor)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Texto fijo
        Text(
            "Recibe todos estos beneficios\ncon nuestro programa de puntos",
            style = MaterialTheme.typography.titleLarge,
            color = TextColor,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TiendaDeRecompensas(
    rewards: List<RewardItem>,
    onRedeem: (String) -> Unit // Recibe la lambda del evento
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Tienda de Recompensas",
            style = MaterialTheme.typography.headlineSmall,
            color = TextColor
        )
        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            maxItemsInEachRow = 2
        ) {
            rewards.forEach { reward ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(CardBackground, RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(reward.name, color = TextColor, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(reward.cost, color = NeonPurple, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { onRedeem(reward.id) }, // Llama al evento
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Canjear")
                    }
                }
            }
        }
    }


}
