package com.bedoya.compartrip.ui.screens.misviajes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bedoya.compartrip.data.local.entity.EntidadReserva
import com.bedoya.compartrip.domain.model.Viaje
import com.bedoya.compartrip.ui.components.TarjetaViaje
import com.bedoya.compartrip.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMisViajes(
    alVolver: () -> Unit,
    alPulsarViaje: (Int) -> Unit,
    viewModel: MisViajesViewModel = hiltViewModel()
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = CremeFondo,
        topBar = {
            TopAppBar(
                title = { Text("Mis Viajes") },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeTurquesa,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
        ) {
            // Pestañas
            TabRow(
                selectedTabIndex = estado.pestanaSeleccionada,
                containerColor = VerdeTurquesa,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Tab(
                    selected = estado.pestanaSeleccionada == 0,
                    onClick = { viewModel.alCambiarPestana(0) },
                    text = { Text("Mis publicaciones") }
                )
                Tab(
                    selected = estado.pestanaSeleccionada == 1,
                    onClick = { viewModel.alCambiarPestana(1) },
                    text = { Text("Mis reservas") }
                )
            }

            when {
                estado.estaCargando -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = VerdeTurquesa)
                    }
                }
                estado.pestanaSeleccionada == 0 -> {
                    PestanaMisPublicaciones(
                        viajes = estado.viajesPublicados,
                        solicitudes = estado.solicitudesRecibidas,
                        alPulsarViaje = alPulsarViaje,
                        alAceptar = viewModel::aceptarSolicitud,
                        alRechazar = viewModel::rechazarSolicitud
                    )
                }
                else -> {
                    PestanaParticipando(
                        viajes = estado.viajesParticipando,
                        alPulsarViaje = alPulsarViaje
                    )
                }
            }
        }
    }
}

@Composable
private fun PestanaMisPublicaciones(
    viajes: List<Viaje>,
    solicitudes: List<EntidadReserva>,
    alPulsarViaje: (Int) -> Unit,
    alAceptar: (EntidadReserva) -> Unit,
    alRechazar: (EntidadReserva) -> Unit
) {
    if (viajes.isEmpty() && solicitudes.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("✈️", fontSize = 48.sp)
                Spacer(Modifier.height(8.dp))
                Text("Aún no has publicado ningún viaje",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTexto)
            }
        }
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Solicitudes pendientes primero
        val pendientes = solicitudes.filter { it.estado == "PENDIENTE" }
        if (pendientes.isNotEmpty()) {
            item {
                Text(
                    "📬 Solicitudes pendientes (${pendientes.size})",
                    style = MaterialTheme.typography.titleMedium,
                    color = VerdeTurquesa,
                    fontWeight = FontWeight.SemiBold
                )
            }
            items(pendientes) { solicitud ->
                TarjetaSolicitud(
                    solicitud = solicitud,
                    alAceptar = { alAceptar(solicitud) },
                    alRechazar = { alRechazar(solicitud) }
                )
            }
        }

        // Mis viajes publicados
        if (viajes.isNotEmpty()) {
            item {
                Spacer(Modifier.height(8.dp))
                Text(
                    "🗺️ Mis viajes publicados",
                    style = MaterialTheme.typography.titleMedium,
                    color = VerdeTurquesa,
                    fontWeight = FontWeight.SemiBold
                )
            }
            items(viajes, key = { it.idViaje }) { viaje ->
                TarjetaViaje(viaje = viaje, alPulsar = { alPulsarViaje(viaje.idViaje) })
            }
        }
    }
}

@Composable
private fun PestanaParticipando(
    viajes: List<Viaje>,
    alPulsarViaje: (Int) -> Unit
) {
    if (viajes.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🧳", fontSize = 48.sp)
                Spacer(Modifier.height(8.dp))
                Text("Aún no participas en ningún viaje",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTexto)
            }
        }
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(viajes, key = { it.idViaje }) { viaje ->
            TarjetaViaje(viaje = viaje, alPulsar = { alPulsarViaje(viaje.idViaje) })
        }
    }
}

@Composable
private fun TarjetaSolicitud(
    solicitud: EntidadReserva,
    alAceptar: () -> Unit,
    alRechazar: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Solicitud de usuario",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "Viaje #${solicitud.idViaje}",
                        style = MaterialTheme.typography.bodySmall,
                        color = GrisTexto
                    )
                }
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFFFF3CD)
                ) {
                    Text(
                        "⏳ Pendiente",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF856404)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = alRechazar,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) { Text("✗ Rechazar") }

                Button(
                    onClick = alAceptar,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeTurquesa)
                ) { Text("✓ Aceptar") }
            }
        }
    }
}