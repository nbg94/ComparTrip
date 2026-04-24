package com.bedoya.compartrip.ui.screens.detalle

import com.bedoya.compartrip.domain.model.Viaje
import com.bedoya.compartrip.domain.model.Usuario

data class EstadoUiDetalle(
    val viaje: Viaje? = null,
    val publicador: Usuario? = null,
    val estaCargando: Boolean = true,
    val error: String? = null,
    val solicitudEnviada: Boolean = false,
    val distanciaKm: Double? = null,
    val duracionMinutos: Double? = null,
    val cargandoRuta: Boolean = false,
    val estadoSolicitud: EstadoSolicitud = EstadoSolicitud.SIN_SOLICITAR,
    val mostrarDialogoSolicitud: Boolean = false
)

enum class EstadoSolicitud {
    SIN_SOLICITAR,   // no ha solicitado aún
    PENDIENTE,       // ya solicitó, esperando respuesta
    ACEPTADA,        // el publicador aceptó
    RECHAZADA        // el publicador rechazó
}