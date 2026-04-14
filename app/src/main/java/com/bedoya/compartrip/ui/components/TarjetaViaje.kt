package com.bedoya.compartrip.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bedoya.compartrip.domain.model.Viaje
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TarjetaViaje(
    viaje: Viaje,
    alPulsar: () -> Unit
) {
    val formatoFecha = SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("es-ES"))
    val fotoPortada = viaje.urlsFotos.getOrNull(
        viaje.urlsFotos.indices.firstOrNull() ?: 0
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { alPulsar() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Foto de portada — solo si existe
            if (fotoPortada != null) {
                AsyncImage(
                    model = fotoPortada,
                    contentDescription = "Foto del viaje",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "${viaje.origen.uppercase()} → ${viaje.destino.uppercase()}",
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
                        label = {
                            Text(when (viaje.tipo.name) {
                                "TRANSPORTE" -> "🚗 Transporte"
                                "ALOJAMIENTO" -> "🏠 Alojamiento"
                                else -> "✈️ Completo"
                            })
                        }
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
}