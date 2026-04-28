package com.bedoya.compartrip.ui.screens.detalle

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.bedoya.compartrip.domain.model.EstadoViaje
import com.bedoya.compartrip.domain.model.TipoViaje
import com.bedoya.compartrip.domain.model.Viaje
import com.bedoya.compartrip.domain.model.Usuario
import com.bedoya.compartrip.SesionUsuario
import com.bedoya.compartrip.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleViaje(
    idViaje: Int,
    alVolver: () -> Unit,
    alContactar: (String) -> Unit,
    viewModel: DetalleViajeViewModel = hiltViewModel()
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    LaunchedEffect(idViaje) {
        viewModel.cargarViaje(idViaje)
    }

    // ---- Diálogo solicitar unirse ----
    if (estado.mostrarDialogoSolicitud) {
        AlertDialog(
            onDismissRequest = viewModel::alCerrarDialogoSolicitud,
            icon = { Text("✈️", fontSize = 32.sp) },
            title = { Text("Solicitar unirse al viaje") },
            text = {
                Text(
                    "¿Confirmas que quieres unirte al viaje de " +
                            "${estado.viaje?.origen} a ${estado.viaje?.destino}?\n\n" +
                            "El publicador recibirá tu solicitud y podrá aceptarla o rechazarla.",
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = viewModel::alConfirmarSolicitud,
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeTurquesa)
                ) { Text("Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = viewModel::alCerrarDialogoSolicitud) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        containerColor = CremeFondo,
        topBar = {
            TopAppBar(
                title = { Text("Detalle del viaje") },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
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
        when {
            estado.estaCargando -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingInterno),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = VerdeTurquesa) }
            }
            estado.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingInterno),
                    contentAlignment = Alignment.Center
                ) {
                    Text(estado.error!!, color = MaterialTheme.colorScheme.error)
                }
            }
            estado.viaje != null -> {
                ContenidoDetalle(
                    viaje = estado.viaje!!,
                    publicador = estado.publicador,
                    estado = estado,
                    paddingInterno = paddingInterno,
                    alSolicitarUnirse = viewModel::alPulsarSolicitarUnirse,
                    alContactar = { alContactar(estado.viaje!!.idPublicador) }
                )
            }
        }
    }
}

@Composable
private fun ContenidoDetalle(
    viaje: Viaje,
    publicador: Usuario?,
    estado: EstadoUiDetalle,
    paddingInterno: PaddingValues,
    alSolicitarUnirse: () -> Unit,
    alContactar: () -> Unit
) {
    val formato = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-ES"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingInterno)
            .verticalScroll(rememberScrollState())
    ) {
        // ---- Carrusel o cabecera ----
        if (viaje.urlsFotos.isNotEmpty()) {
            CarruselFotos(fotos = viaje.urlsFotos)
        }

        // Origen → destino siempre visible
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(VerdeTurquesa)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${viaje.origen}  →  ${viaje.destino}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formato.format(Date(viaje.fecha)),
                    style = MaterialTheme.typography.bodyMedium,
                    color = VerdeTurquesaClaro
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = VerdeTurquesaOscuro
                ) {
                    Text(
                        text = when (viaje.tipo) {
                            TipoViaje.TRANSPORTE -> "🚗 Solo transporte"
                            TipoViaje.ALOJAMIENTO -> "🏠 Solo alojamiento"
                            TipoViaje.COMPLETO -> "✈️ Transporte + Alojamiento"
                        },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ---- Info rápida ----
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    InfoRapida("💺", "${viaje.plazasDisponibles}", "Plazas")
                    VerticalDivider(modifier = Modifier.height(48.dp))
                    InfoRapida(
                        "💶",
                        if (viaje.precio != null) "${viaje.precio}€" else "Gratis",
                        "Precio"
                    )
                    VerticalDivider(modifier = Modifier.height(48.dp))
                    InfoRapida(
                        if (viaje.estado == EstadoViaje.ACTIVO) "✅" else "❌",
                        when (viaje.estado) {
                            EstadoViaje.ACTIVO -> "Disponible"
                            EstadoViaje.COMPLETADO -> "Completo"
                            EstadoViaje.CANCELADO -> "Cancelado"
                        },
                        "Estado"
                    )
                }
            }

                // ---- Distancia y duración ---- añade desde aquí
                if (estado.cargandoRuta) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = VerdeTurquesa
                            )
                            Text(
                                "Calculando ruta...",
                                style = MaterialTheme.typography.bodySmall,
                                color = GrisTexto
                            )
                        }
                    }
                }
                if (estado.distanciaKm != null && estado.duracionMinutos != null) {
                    TarjetaRuta(
                        distanciaKm = estado.distanciaKm!!,
                        duracionMinutos = estado.duracionMinutos!!
                    )
                }


            // ---- Descripción ----
            if (viaje.descripcion.isNotBlank()) {
                SeccionDetalle(titulo = "📋 Descripción") {
                    Text(
                        text = viaje.descripcion,
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrisTexto
                    )
                }
            }

            // ---- Preferencias ----
            SeccionDetalle(titulo = "⚙️ Preferencias") {
                FilaPreferencia("🚬 Fumadores", viaje.admiteFumadores)
                FilaPreferencia("🐾 Mascotas", viaje.admiteMascotas)
                if (viaje.soloMujeres) FilaPreferencia("👩 Solo mujeres", true)
                if (viaje.soloHombres) FilaPreferencia("👨 Solo hombres", true)
                if (viaje.edadMinima != null || viaje.edadMaxima != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("🎂 Rango de edad",
                            style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "${viaje.edadMinima ?: "?"} - ${viaje.edadMaxima ?: "?"} años",
                            style = MaterialTheme.typography.bodyMedium,
                            color = VerdeTurquesa
                        )
                    }
                }
            }

            // ---- Publicador ----
            SeccionDetalle(titulo = "👤 Publicado por") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(VerdeTurquesa),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = publicador?.nombre?.firstOrNull()?.toString() ?: "?",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column {
                        val nombreMostrar = publicador?.nombre?.let {
                            if (it.contains("@")) "Usuario Compartrip" else it
                        } ?: "Usuario"
                        Text(
                            text = nombreMostrar,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        if ((publicador?.valoracionMedia ?: 0f) > 0f) {
                            Text(
                                text = "⭐ ${publicador?.valoracionMedia}",
                                style = MaterialTheme.typography.bodySmall,
                                color = GrisTexto
                            )
                        }
                    }
                }
            }

            // ---- Botones de acción ----
            Spacer(modifier = Modifier.height(8.dp))

            // ---- Botones de acción ----
            Spacer(modifier = Modifier.height(8.dp))

            val esMiViaje = estado.viaje?.idPublicador == SesionUsuario.idActual

            when {
                esMiViaje -> {
                    // El publicador ve un botón diferente
                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Este es tu viaje publicado ✅")
                    }
                }
                estado.estadoSolicitud == EstadoSolicitud.PENDIENTE -> {
                    Button(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GrisTexto),
                        enabled = false
                    ) {
                        Text("Solicitud enviada ⏳", style = MaterialTheme.typography.labelLarge)
                    }
                }
                else -> {
                    Button(
                        onClick = alSolicitarUnirse,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeTurquesa)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Solicitar unirse", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }

            OutlinedButton(
                onClick = alContactar,    // ← en vez del TODO
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Email, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Contactar", style = MaterialTheme.typography.labelLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ---- Componentes privados ----

@Composable
private fun SeccionDetalle(
    titulo: String,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                color = VerdeTurquesa,
                fontWeight = FontWeight.SemiBold
            )
            contenido()
        }
    }
}

@Composable
private fun InfoRapida(icono: String, valor: String, etiqueta: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = icono, style = MaterialTheme.typography.titleLarge)
        Text(text = valor, style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold)
        Text(text = etiqueta, style = MaterialTheme.typography.bodySmall,
            color = GrisTexto)
    }
}

@Composable
private fun FilaPreferencia(etiqueta: String, activado: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(etiqueta, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = if (activado) "Sí ✅" else "No ❌",
            style = MaterialTheme.typography.bodyMedium,
            color = if (activado) VerdeTurquesa else GrisTexto
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CarruselFotos(fotos: List<String>) {
    val pagerState = rememberPagerState(pageCount = { fotos.size })

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { pagina ->
            AsyncImage(
                model = fotos[pagina],
                contentDescription = "Foto ${pagina + 1}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            fotos.indices.forEach { indice ->
                Box(
                    modifier = Modifier
                        .size(if (indice == pagerState.currentPage) 10.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (indice == pagerState.currentPage) Color.White
                            else Color.White.copy(alpha = 0.5f)
                        )
                )
            }
        }
    }
}

@Composable
private fun TarjetaRuta(distanciaKm: Double, duracionMinutos: Double) {
    val horas = (duracionMinutos / 60).toInt()
    val minutos = (duracionMinutos % 60).toInt()
    val duracionTexto = if (horas > 0) "${horas}h ${minutos}min" else "${minutos}min"

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("📍", fontSize = 24.sp)
                Text(
                    "${String.format("%.0f", distanciaKm)} km",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = VerdeTurquesa
                )
                Text("Distancia",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrisTexto)
            }
            VerticalDivider(modifier = Modifier.height(48.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("⏱️", fontSize = 24.sp)
                Text(
                    duracionTexto,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = VerdeTurquesa
                )
                Text("Duración estimada",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrisTexto)
            }
        }
    }
}

