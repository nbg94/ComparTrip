package com.bedoya.compartrip.domain.model

data class Reserva(
    val idReserva: Int = 0,
    val idViaje: Int,
    val idUsuario: String,
    val estado: EstadoReserva = EstadoReserva.PENDIENTE
)

enum class EstadoReserva {
    PENDIENTE,
    ACEPTADA,
    RECHAZADA,
    CANCELADA
}