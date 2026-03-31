package com.bedoya.compartrip.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "viajes",
    foreignKeys = [
        ForeignKey(
            entity = EntidadUsuario::class,
            parentColumns = ["idUsuario"],
            childColumns = ["idPublicador"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idPublicador")]
)
data class EntidadViaje(
    @PrimaryKey(autoGenerate = true)
    val idViaje: Int = 0,
    val idPublicador: String,
    val origen: String,
    val destino: String,
    val fecha: Long,
    val tipo: String,
    val plazasDisponibles: Int,
    val descripcion: String,
    val precio: Double? = null,
    val admiteFumadores: Boolean = false,
    val generoPreferido: String? = null,
    val edadMinimaPreferida: Int? = null,
    val edadMaximaPreferida: Int? = null,
    val estado: String = "ACTIVO",
    val creadoEn: Long = System.currentTimeMillis()
)