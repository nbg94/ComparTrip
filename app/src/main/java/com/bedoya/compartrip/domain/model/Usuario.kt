package com.bedoya.compartrip.domain.model

data class Usuario(
    val idUsuario: String,
    val nombre: String,
    val correo: String,
    val urlFoto: String? = null,
    val biografia: String? = null,
    val valoracionMedia: Float = 0f
)