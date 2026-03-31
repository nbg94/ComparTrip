package com.bedoya.compartrip.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reservas",
    foreignKeys = [
        ForeignKey(
            entity = EntidadViaje::class,
            parentColumns = ["idViaje"],
            childColumns = ["idViaje"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EntidadUsuario::class,
            parentColumns = ["idUsuario"],
            childColumns = ["idUsuario"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idViaje"), Index("idUsuario")]
)
data class EntidadReserva(
    @PrimaryKey(autoGenerate = true)
    val idReserva: Int = 0,
    val idViaje: Int,
    val idUsuario: String,
    val estado: String = "PENDIENTE",  // "PENDIENTE", "ACEPTADA", "RECHAZADA", "CANCELADA"
    val solicitadoEn: Long = System.currentTimeMillis(),
    val actualizadoEn: Long = System.currentTimeMillis()
)