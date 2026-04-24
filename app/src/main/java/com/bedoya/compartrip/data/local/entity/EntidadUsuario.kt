package com.bedoya.compartrip.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class EntidadUsuario(
    @PrimaryKey
    val idUsuario: String,
    val nombre: String,
    val correo: String,
    val urlFoto: String? = null,
    val biografia: String? = null,
    val valoracionMedia: Float = 0f,
    val creadoEn: Long = System.currentTimeMillis(),
    // ---- campos nuevos ----
    val totalViajes: Int = 0,
    val dniVerificado: Boolean = false
)