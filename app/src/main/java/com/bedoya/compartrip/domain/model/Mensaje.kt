package com.bedoya.compartrip.domain.model

data class Mensaje(
    val idMensaje: Int = 0,
    val idViaje: Int,
    val idEmisor: String,
    val idReceptor: String,
    val contenido: String,
    val enviadoEn: Long,
    val leido: Boolean = false
)