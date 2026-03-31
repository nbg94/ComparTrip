package com.bedoya.compartrip.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bedoya.compartrip.domain.model.Viaje
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TarjetaViaje(
    viaje: Viaje,
    alPulsar: () -> Unit
) {
    val formatoFecha = SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("es-ES"))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { alPulsar() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${viaje.origen} → ${viaje.destino}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatoFecha.format(Date(viaje.fecha)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(
                    onClick = {},
                    label = { Text(viaje.tipo.name) }
                )
                AssistChip(
                    onClick = {},
                    label = { Text("${viaje.plazasDisponibles} plazas") }
                )
            }
            if (viaje.descripcion.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = viaje.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
            }
        }
    }
}