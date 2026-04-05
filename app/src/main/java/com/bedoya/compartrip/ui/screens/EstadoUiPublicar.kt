package com.bedoya.compartrip.ui.screens

import com.bedoya.compartrip.domain.model.TipoViaje

data class EstadoUiPublicar(
    val origen: String = "",
    val destino: String = "",
    val fechaIda: Long? = null,
    val fechaVuelta: Long? = null,      // nueva — para alojamiento
    val tipo: TipoViaje = TipoViaje.TRANSPORTE,
    val plazas: String = "",
    val descripcion: String = "",
    val precio: String = "",
    val admiteFumadores: Boolean = false,
    val admiteMascotas: Boolean = false,  // nueva
    val soloMujeres: Boolean = false,     // nueva — preferencia de género
    val estaCargando: Boolean = false,
    val error: String? = null,
    val publicadoConExito: Boolean = false
)