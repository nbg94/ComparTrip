package com.bedoya.compartrip.ui.screens.publicar

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bedoya.compartrip.domain.model.TipoViaje
import com.bedoya.compartrip.ui.screens.PublicarViajeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPublicarViaje(
    alVolver: () -> Unit,
    alPublicar: () -> Unit,
    viewModel: PublicarViajeViewModel = hiltViewModel()
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    // Navegar de vuelta cuando se publica con éxito
    LaunchedEffect(estado.publicadoConExito) {
        if (estado.publicadoConExito) alPublicar()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publicar viaje") },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = estado.origen,
                onValueChange = viewModel::alCambiarOrigen,
                label = { Text("Origen") },
                modifier = Modifier.fillMaxWidth(),
                isError = estado.origen.isBlank() && estado.error != null
            )
            OutlinedTextField(
                value = estado.destino,
                onValueChange = viewModel::alCambiarDestino,
                label = { Text("Destino") },
                modifier = Modifier.fillMaxWidth(),
                isError = estado.destino.isBlank() && estado.error != null
            )
            OutlinedTextField(
                value = estado.plazas,
                onValueChange = viewModel::alCambiarPlazas,
                label = { Text("Plazas disponibles") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = estado.descripcion,
                onValueChange = viewModel::alCambiarDescripcion,
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // Selector de tipo
            Text("¿Qué quieres compartir?", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TipoViaje.entries.forEach { tipo ->
                    FilterChip(
                        selected = estado.tipo == tipo,
                        onClick = { viewModel.alCambiarTipo(tipo) },
                        label = { Text(tipo.name) }
                    )
                }
            }

            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Checkbox(
                    checked = estado.admiteFumadores,
                    onCheckedChange = viewModel::alCambiarFumadores
                )
                Text("Admite fumadores")
            }

            if (estado.error != null) {
                Text(
                    text = estado.error!!,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = { viewModel.alPublicar("usuario_temporal") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !estado.estaCargando
            ) {
                if (estado.estaCargando) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Publicar")
                }
            }
        }
    }
}