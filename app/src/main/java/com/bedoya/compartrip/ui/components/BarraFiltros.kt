package com.bedoya.compartrip.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BarraFiltros(
    filtroSeleccionado: String,
    alSeleccionarFiltro: (String) -> Unit
) {
    val filtros = listOf(
        "" to "Todos",
        "TRANSPORTE" to "Transporte",
        "ALOJAMIENTO" to "Alojamiento",
        "COMPLETO" to "Completo"
    )

    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            // horizontalScroll → permite deslizar si no caben todos los chips
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filtros.forEach { (valor, etiqueta) ->
            FilterChip(
                selected = filtroSeleccionado == valor,
                onClick = { alSeleccionarFiltro(valor) },
                label = { Text(etiqueta) }
            )
        }
    }
}