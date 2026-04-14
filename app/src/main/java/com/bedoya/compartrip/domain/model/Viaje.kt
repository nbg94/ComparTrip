package com.bedoya.compartrip.domain.model

data class Viaje(
    val idViaje: Int = 0,
    val idPublicador: String,
    val origen: String,
    val destino: String,
    val fecha: Long,
    val tipo: TipoViaje,         // usamos un enum, no un String → más seguro
    val plazasDisponibles: Int,
    val descripcion: String,
    val precio: Double? = null,
    val admiteFumadores: Boolean = false,
    val generoPreferido: String? = null,
    val edadMinimaPreferida: Int? = null,
    val edadMaximaPreferida: Int? = null,
    val estado: EstadoViaje = EstadoViaje.ACTIVO,
    val admiteMascotas: Boolean = false,
    val soloMujeres: Boolean = false,
    val soloHombres: Boolean = false,
    val edadMinima: Int? = null,
    val edadMaxima: Int? = null,
    val urlsFotos: List<String> = emptyList()
)

// Enum → en vez de Strings sueltos ("ACTIVO", "CANCELADO") usamos valores controlados
// Si escribes mal un String nadie te avisa. Si escribes mal un enum, el compilador te avisa.
enum class TipoViaje {
    TRANSPORTE,
    ALOJAMIENTO,
    COMPLETO
}

enum class EstadoViaje {
    ACTIVO,
    COMPLETADO,
    CANCELADO
}