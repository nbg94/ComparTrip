package com.bedoya.compartrip.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bedoya.compartrip.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaChat(
    idViaje: Int,
    idReceptor: String,
    alVolver: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(idViaje) {
        viewModel.iniciarChat(idViaje, idReceptor)
    }

    // Scroll automático al último mensaje
    LaunchedEffect(estado.mensajes.size) {
        if (estado.mensajes.isNotEmpty()) {
            listState.animateScrollToItem(estado.mensajes.size - 1)
        }
    }

    Scaffold(
        containerColor = CremeFondo,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Chat del viaje #$idViaje")
                        Text(
                            if (estado.conectado) "● Conectado" else "○ Desconectado",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (estado.conectado) Color(0xFF4CAF50)
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
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
        },
        bottomBar = {
            // Campo de texto y botón enviar
            Surface(
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .navigationBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = estado.textoActual,
                        onValueChange = viewModel::alCambiarTexto,
                        placeholder = { Text("Escribe un mensaje...") },
                        modifier = Modifier.weight(1f),
                        maxLines = 3,
                        shape = RoundedCornerShape(24.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(
                            onSend = { viewModel.enviarMensaje() }
                        )
                    )
                    FloatingActionButton(
                        onClick = viewModel::enviarMensaje,
                        containerColor = VerdeTurquesa,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Enviar",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    ) { paddingInterno ->
        when {
            estado.estaCargando -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingInterno.calculateTopPadding()),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = VerdeTurquesa) }
            }
            estado.mensajes.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingInterno.calculateTopPadding()),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("💬", fontSize = 48.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Aún no hay mensajes.\n¡Empieza la conversación!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrisTexto,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingInterno.calculateTopPadding()),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(estado.mensajes) { mensaje ->
                        BurbujaMensaje(mensaje = mensaje)
                    }
                }
            }
        }
    }
}

@Composable
private fun BurbujaMensaje(mensaje: MensajeUi) {
    val formato = SimpleDateFormat("HH:mm", Locale.forLanguageTag("es-ES"))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (mensaje.esMio)
            Arrangement.End else Arrangement.Start
    ) {
        Column(
            horizontalAlignment = if (mensaje.esMio)
                Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            if (!mensaje.esMio) {
                Text(
                    text = mensaje.nombreEmisor,
                    style = MaterialTheme.typography.labelSmall,
                    color = GrisTexto,
                    modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                )
            }
            Box(
                modifier = Modifier
                    .background(
                        color = if (mensaje.esMio) VerdeTurquesa
                        else MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (mensaje.esMio) 16.dp else 4.dp,
                            bottomEnd = if (mensaje.esMio) 4.dp else 16.dp
                        )
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = mensaje.contenido,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (mensaje.esMio) Color.White
                    else MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = formato.format(Date(mensaje.enviadoEn)),
                style = MaterialTheme.typography.labelSmall,
                color = GrisTexto,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 2.dp)
            )
        }
    }
}