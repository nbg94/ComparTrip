package com.bedoya.compartrip.domain.model

data class Resena(
    val idResena: Int = 0,
    val idReserva: Int,
    val idResenador: String,
    val idResenado: String,
    val puntuacion: Int,          // 1 a 5
    val comentario: String? = null
)