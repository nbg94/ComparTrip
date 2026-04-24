package com.bedoya.compartrip.ui.screens

import com.bedoya.compartrip.domain.model.Viaje

// Representa todos los posibles estados de la pantalla principal
data class EstadoUiHome(
    val viajes: List<Viaje> = emptyList(),
    val estaCargando: Boolean = false,
    val error: String? = null,
    val textoBusqueda: String = "",
    val filtroTipo: String = "",
    val modoBusqueda: String = "DESTINO"  // ← nuevo
)