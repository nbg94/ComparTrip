package com.bedoya.compartrip.ui.screens.mensajes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bedoya.compartrip.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMensajes(
    alPulsarConversacion: (Int, String) -> Unit,
    viewModel: MensajesViewModel = hiltViewModel()
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = CremeFondo,
        topBar = {
            TopAppBar(
                title = { Text("Mensajes") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeTurquesa,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingInterno ->
        when {
            estado.estaCargando -> {
                Box(
                    Modifier.fillMaxSize().padding(paddingInterno),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = VerdeTurquesa) }
            }
            estado.conversaciones.isEmpty() -> {
                Box(
                    Modifier.fillMaxSize().padding(paddingInterno),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("💬", fontSize = 48.sp)
                        Text(
                            "No tienes mensajes aún",
                            style = MaterialTheme.typography.titleMedium,
                            color = GrisTexto
                        )
                        Text(
                            "Cuando contactes con un viajero\naparecerá aquí tu conversación",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrisTexto,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingInterno),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(estado.conversaciones) { conv ->
                        TarjetaConversacion(
                            conversacion = conv,
                            alPulsar = {
                                alPulsarConversacion(conv.idViaje, conv.idReceptor)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaConversacion(
    conversacion: ConversacionUi,
    alPulsar: () -> Unit
) {
    val formato = SimpleDateFormat("HH:mm", Locale.forLanguageTag("es-ES"))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { alPulsar() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Avatar con inicial
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .then(
                        if (conversacion.noLeidos > 0)
                            Modifier.padding(2.dp)
                        else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.size(50.dp),
                    shape = CircleShape,
                    color = VerdeTurquesa
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = conversacion.nombreViaje.firstOrNull()?.toString() ?: "?",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = conversacion.nombreViaje,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (conversacion.noLeidos > 0)
                            FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = formato.format(Date(conversacion.horaUltimo)),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (conversacion.noLeidos > 0) VerdeTurquesa else GrisTexto
                    )
                }
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversacion.ultimoMensaje,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (conversacion.noLeidos > 0)
                            MaterialTheme.colorScheme.onSurface else GrisTexto,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (conversacion.noLeidos > 0) {
                        Badge(containerColor = VerdeTurquesa) {
                            Text(
                                "${conversacion.noLeidos}",
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}