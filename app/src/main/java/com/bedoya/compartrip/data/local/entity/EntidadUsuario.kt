package com.bedoya.compartrip.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class EntidadUsuario(
    @PrimaryKey
    val idUsuario: String,           // viene de Auth0
    val nombre: String,
    val correo: String,
    val urlFoto: String? = null,     // null = no tiene foto
    val biografia: String? = null,
    val valoracionMedia: Float = 0f,
    val creadoEn: Long = System.currentTimeMillis()
)