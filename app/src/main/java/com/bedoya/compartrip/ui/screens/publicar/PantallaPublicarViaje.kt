package com.bedoya.compartrip.ui.screens.publicar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bedoya.compartrip.SesionUsuario
import com.bedoya.compartrip.domain.model.TipoViaje
import com.bedoya.compartrip.ui.screens.PublicarViajeViewModel
import com.bedoya.compartrip.ui.theme.CremeFondo
import com.bedoya.compartrip.ui.theme.VerdeTurquesa
import com.bedoya.compartrip.ui.theme.VerdeTurquesaClaro
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPublicarViaje(
    alVolver: () -> Unit,
    alPublicar: () -> Unit,
    viewModel: PublicarViajeViewModel = hiltViewModel()
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    var mostrarSelectorFechaIda by remember { mutableStateOf(false) }
    var mostrarSelectorFechaVuelta by remember { mutableStateOf(false) }

    val necesitaFechaVuelta = estado.tipo == TipoViaje.ALOJAMIENTO || estado.tipo == TipoViaje.COMPLETO
    val necesitaPlazas = estado.tipo == TipoViaje.TRANSPORTE || estado.tipo == TipoViaje.COMPLETO

    LaunchedEffect(estado.publicadoConExito) {
        if (estado.publicadoConExito) alPublicar()
    }

    // Selector fecha ida
    if (mostrarSelectorFechaIda) {
        SelectorFecha(
            fechaInicial = estado.fechaIda,
            onAceptar = { viewModel.alCambiarFechaIda(it); mostrarSelectorFechaIda = false },
            onCancelar = { mostrarSelectorFechaIda = false }
        )
    }

    // Selector fecha vuelta
    if (mostrarSelectorFechaVuelta) {
        SelectorFecha(
            fechaInicial = estado.fechaVuelta,
            onAceptar = { viewModel.alCambiarFechaVuelta(it); mostrarSelectorFechaVuelta = false },
            onCancelar = { mostrarSelectorFechaVuelta = false }
        )
    }

    Scaffold(
        containerColor = CremeFondo,
        topBar = {
            TopAppBar(
                title = { Text("Publicar nuevo viaje") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ---- SECCIÓN: Destino ----
            SeccionTitulo("📍 ¿A dónde vas?")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CampoTexto(
                    valor = estado.origen,
                    alCambiar = viewModel::alCambiarOrigen,
                    etiqueta = "Origen",
                    modifier = Modifier.weight(1f),
                    hayError = estado.origen.isBlank() && estado.error != null
                )
                CampoTexto(
                    valor = estado.destino,
                    alCambiar = viewModel::alCambiarDestino,
                    etiqueta = "Destino",
                    modifier = Modifier.weight(1f),
                    hayError = estado.destino.isBlank() && estado.error != null
                )
            }

            // ---- Fechas ----
            val formato = SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("es-ES"))

            // Fecha ida — siempre visible
            CampoFecha(
                etiqueta = if (necesitaFechaVuelta) "Fecha de ida" else "Fecha del viaje",
                fecha = estado.fechaIda,
                formato = formato,
                alPulsar = { mostrarSelectorFechaIda = true }
            )

            // Fecha vuelta — solo si hay alojamiento
            if (necesitaFechaVuelta) {
                CampoFecha(
                    etiqueta = "Fecha de vuelta",
                    fecha = estado.fechaVuelta,
                    formato = formato,
                    alPulsar = { mostrarSelectorFechaVuelta = true }
                )
            }

            HorizontalDivider(color = VerdeTurquesaClaro)

            // ---- SECCIÓN: Qué compartes ----
            SeccionTitulo("🤝 ¿Qué quieres compartir?")

            Card(
                colors = CardDefaults.cardColors(containerColor = VerdeTurquesaClaro),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    TipoViaje.entries.forEach { tipo ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = estado.tipo == tipo,
                                onCheckedChange = { if (it) viewModel.alCambiarTipo(tipo) },
                                colors = CheckboxDefaults.colors(checkedColor = VerdeTurquesa)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (tipo) {
                                    TipoViaje.TRANSPORTE -> "🚗 Solo transporte"
                                    TipoViaje.ALOJAMIENTO -> "🏠 Solo alojamiento"
                                    TipoViaje.COMPLETO -> "✈️ Transporte + Alojamiento"
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = VerdeTurquesaClaro)

            // ---- SECCIÓN: Detalles ----
            SeccionTitulo("📋 Detalles del viaje")

            // Plazas — solo visible si hay transporte
            if (necesitaPlazas) {
                OutlinedTextField(
                    value = estado.plazas,
                    onValueChange = viewModel::alCambiarPlazas,
                    label = { Text("Lugares disponibles en el coche") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None
                    )
                )
            }

            // Descripción — sin autocorrección ni mayúsculas automáticas
            OutlinedTextField(
                value = estado.descripcion,
                onValueChange = viewModel::alCambiarDescripcion,
                label = { Text("Descripción") },
                placeholder = { Text("Cuéntanos sobre tu viaje, horarios, preferencias...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                minLines = 4,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                    // Sentences → solo la primera letra de cada frase en mayúscula
                    // Words → cada palabra en mayúscula (era lo que te molestaba)
                )
            )

            OutlinedTextField(
                value = estado.precio,
                onValueChange = viewModel::alCambiarPrecio,
                label = { Text("Precio orientativo (opcional)") },
                placeholder = { Text("Ej: 15") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                suffix = { Text("€") }
            )

            HorizontalDivider(color = VerdeTurquesaClaro)

            // ---- SECCIÓN: Preferencias ----
            SeccionTitulo("⚙️ Preferencias")

            Card(
                colors = CardDefaults.cardColors(containerColor = VerdeTurquesaClaro),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    FilaPreferencia(
                        texto = "🚬 Admite fumadores",
                        activado = estado.admiteFumadores,
                        alCambiar = viewModel::alCambiarFumadores
                    )
                    HorizontalDivider(color = VerdeTurquesa.copy(alpha = 0.2f))
                    FilaPreferencia(
                        texto = "🐾 Admite mascotas",
                        activado = estado.admiteMascotas,
                        alCambiar = viewModel::alCambiarMascotas
                    )
                    HorizontalDivider(color = VerdeTurquesa.copy(alpha = 0.2f))
                    FilaPreferencia(
                        texto = "👩 Solo mujeres",
                        activado = estado.soloMujeres,
                        alCambiar = viewModel::alCambiarSoloMujeres
                    )
                    // Dentro del Card de preferencias, después de soloMujeres:
                    HorizontalDivider(color = VerdeTurquesa.copy(alpha = 0.2f))
                    FilaPreferencia(
                        texto = "👨 Solo hombres",
                        activado = estado.soloHombres,
                        alCambiar = viewModel::alCambiarSoloHombres
                    )
                    HorizontalDivider(color = VerdeTurquesa.copy(alpha = 0.2f))

// Rango de edades
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("🎂 Rango de edad (opcional)",
                            style = MaterialTheme.typography.bodyMedium)
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = estado.edadMinima,
                            onValueChange = viewModel::alCambiarEdadMinima,
                            label = { Text("Edad mín.") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            suffix = { Text("años") }
                        )
                        OutlinedTextField(
                            value = estado.edadMaxima,
                            onValueChange = viewModel::alCambiarEdadMaxima,
                            label = { Text("Edad máx.") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            suffix = { Text("años") }
                        )
                    }
                }
            }

            // ---- Error ----
            if (estado.error != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "⚠️ ${estado.error}",
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // ---- Botones ----
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = alVolver,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Cancelar") }

                Button(
                    onClick = { viewModel.alPublicar(SesionUsuario.idActual) },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeTurquesa),
                    enabled = !estado.estaCargando
                ) {
                    if (estado.estaCargando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Publicar")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ---- Componentes privados reutilizables ----

@Composable
private fun SeccionTitulo(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleMedium,
        color = VerdeTurquesa
    )
}

@Composable
private fun CampoTexto(
    valor: String,
    alCambiar: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    hayError: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        label = { Text(etiqueta) },
        modifier = modifier,
        singleLine = true,
        isError = hayError,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words
            // Words en origen/destino sí tiene sentido (nombres de ciudades)
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CampoFecha(
    etiqueta: String,
    fecha: Long?,
    formato: java.text.SimpleDateFormat,
    alPulsar: () -> Unit
) {
    OutlinedTextField(
        value = if (fecha != null) formato.format(java.util.Date(fecha)) else "",
        onValueChange = {},
        label = { Text(etiqueta) },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = alPulsar) {
                Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectorFecha(
    fechaInicial: Long?,
    onAceptar: (Long) -> Unit,
    onCancelar: () -> Unit
) {
    val selectorEstado = rememberDatePickerState(
        initialSelectedDateMillis = fechaInicial ?: System.currentTimeMillis()
    )
    DatePickerDialog(
        onDismissRequest = onCancelar,
        confirmButton = {
            TextButton(onClick = {
                selectorEstado.selectedDateMillis?.let { onAceptar(it) }
            }) { Text("Aceptar") }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        }
    ) {
        DatePicker(state = selectorEstado)
    }
}

@Composable
private fun FilaPreferencia(
    texto: String,
    activado: Boolean,
    alCambiar: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(texto, style = MaterialTheme.typography.bodyMedium)
        Switch(
            checked = activado,
            onCheckedChange = alCambiar,
            colors = SwitchDefaults.colors(checkedThumbColor = VerdeTurquesa)
        )
    }
}