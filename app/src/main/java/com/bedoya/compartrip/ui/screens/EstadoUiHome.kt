package com.bedoya.compartrip.ui.screens

import com.bedoya.compartrip.domain.model.Viaje

// Representa todos los posibles estados de la pantalla principal
data class EstadoUiHome(
    val viajes: List<Viaje> = emptyList(),  // lista de viajes a mostrar
    val estaCargando: Boolean = false,       // true = mostramos un spinner
    val error: String? = null,              // null = no hay error
    val textoBusqueda: String = "",         // lo que el usuario escribe en el buscador
    val filtroTipo: String = ""             // "TRANSPORTE", "ALOJAMIENTO", "COMPLETO" o "" (todos)
)