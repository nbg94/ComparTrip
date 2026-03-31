package com.bedoya.compartrip.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bedoya.compartrip.ui.screens.HomeViewModel
import com.bedoya.compartrip.ui.components.TarjetaViaje
import com.bedoya.compartrip.ui.components.BarraFiltros

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaHome(
    alPulsarPublicar: () -> Unit,
    alPulsarViaje: (Int) -> Unit,
    alPulsarPerfil: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
    // hiltViewModel() → Hilt crea e inyecta el ViewModel automáticamente
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    // collectAsStateWithLifecycle → observa el Flow respetando el ciclo de vida
    // cuando la pantalla no está visible deja de observar (ahorra batería)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compartrip") },
                actions = {
                    TextButton(onClick = alPulsarPerfil) {
                        Text("Perfil")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = alPulsarPublicar) {
                Icon(Icons.Default.Add, contentDescription = "Publicar viaje")
            }
        }
    ) { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
        ) {
            // Barra de búsqueda
            OutlinedTextField(
                value = estado.textoBusqueda,
                onValueChange = viewModel::alCambiarBusqueda,
                placeholder = { Text("Buscar destino...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true
            )

            // Chips de filtro
            BarraFiltros(
                filtroSeleccionado = estado.filtroTipo,
                alSeleccionarFiltro = viewModel::alCambiarFiltro
            )

            // Contenido principal
            when {
                estado.estaCargando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                estado.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error: ${estado.error}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                estado.viajes.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay viajes disponibles")
                    }
                }
                else -> {
                    // LazyColumn → como un RecyclerView pero en Compose
                    // solo renderiza los elementos visibles en pantalla
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = estado.viajes,
                            key = { viaje -> viaje.idViaje }
                            // key → ayuda a Compose a identificar cada elemento
                            // si la lista cambia, solo redibuja los elementos que cambiaron
                        ) { viaje ->
                            TarjetaViaje(
                                viaje = viaje,
                                alPulsar = { alPulsarViaje(viaje.idViaje) }
                            )
                        }
                    }
                }
            }
        }
    }
}