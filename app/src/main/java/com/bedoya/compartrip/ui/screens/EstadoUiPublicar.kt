package com.bedoya.compartrip.ui.screens

import com.bedoya.compartrip.domain.model.TipoViaje

data class EstadoUiPublicar(
    val origen: String = "",
    val destino: String = "",
    val fecha: Long? = null,
    val tipo: TipoViaje = TipoViaje.COMPLETO,
    val plazas: String = "",               // String porque viene de un campo de texto
    val descripcion: String = "",
    val precio: String = "",
    val admiteFumadores: Boolean = false,
    val estaCargando: Boolean = false,
    val error: String? = null,
    val publicadoConExito: Boolean = false  // true = navegamos de vuelta al home
)