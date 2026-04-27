package com.bedoya.compartrip.ui.screens.misviajes

import com.bedoya.compartrip.data.local.entity.EntidadReserva
import com.bedoya.compartrip.domain.model.Viaje

data class EstadoUiMisViajes(
    val viajesPublicados: List<Viaje> = emptyList(),
    val viajesParticipando: List<Viaje> = emptyList(),
    val solicitudesRecibidas: List<EntidadReserva> = emptyList(),
    val estaCargando: Boolean = true,
    val error: String? = null,
    val pestanaSeleccionada: Int = 0  // 0 = publicados, 1 = participando
)