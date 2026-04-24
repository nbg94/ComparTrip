package com.bedoya.compartrip.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.bedoya.compartrip.data.remote.dto.DtoCiudad
import com.bedoya.compartrip.ui.theme.VerdeTurquesa
import com.bedoya.compartrip.ui.theme.VerdeTurquesaClaro
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CampoAutocompletadoCiudad(
    valor: String,
    etiqueta: String,
    alCambiar: (String) -> Unit,
    alBuscarCiudades: suspend (String) -> List<DtoCiudad>,
    modifier: Modifier = Modifier,
    hayError: Boolean = false,
    placeholder: String? = null,          // ← nuevo opcional
    mostrarIconoBusqueda: Boolean = false, // ← nuevo opcional
    mostrarBotonBorrar: Boolean = false    // ← nuevo opcional
) {
    var sugerencias by remember { mutableStateOf<List<DtoCiudad>>(emptyList()) }
    var mostrarSugerencias by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var trabajoBusqueda: Job? by remember { mutableStateOf(null) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = valor,
            onValueChange = { texto ->
                alCambiar(texto)
                trabajoBusqueda?.cancel()
                trabajoBusqueda = scope.launch {
                    delay(300)
                    if (texto.length >= 2) {
                        val resultado = alBuscarCiudades(texto)
                        sugerencias = resultado
                        mostrarSugerencias = resultado.isNotEmpty()
                    } else {
                        sugerencias = emptyList()
                        mostrarSugerencias = false
                    }
                }
            },
            label = if (etiqueta.isNotBlank()) {{ Text(etiqueta) }} else null,
            placeholder = if (placeholder != null) {{ Text(placeholder) }} else null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = hayError,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            ),
            leadingIcon = if (mostrarIconoBusqueda) {
                { Icon(Icons.Default.Search, contentDescription = null, tint = VerdeTurquesa) }
            } else null,
            trailingIcon = if (mostrarBotonBorrar && valor.isNotBlank()) {
                {
                    IconButton(onClick = {
                        alCambiar("")
                        sugerencias = emptyList()
                        mostrarSugerencias = false
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Borrar")
                    }
                }
            } else null
        )

        if (mostrarSugerencias && sugerencias.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                    items(sugerencias) { ciudad ->
                        val nombreCiudad = ciudad.direccion?.ciudad
                            ?: ciudad.direccion?.pueblo
                            ?: ciudad.direccion?.aldea
                            ?: ciudad.nombreCompleto.split(",").firstOrNull()?.trim()
                            ?: ciudad.nombreCompleto
                        val pais = ciudad.direccion?.pais ?: ""

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    alCambiar(nombreCiudad)
                                    mostrarSugerencias = false
                                    sugerencias = emptyList()
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Text(nombreCiudad, style = MaterialTheme.typography.bodyMedium)
                            if (pais.isNotBlank()) {
                                Text(pais, style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        HorizontalDivider(color = VerdeTurquesaClaro)
                    }
                }
            }
        }
    }
}