package com.bedoya.compartrip.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "mensajes",
    foreignKeys = [
        ForeignKey(
            entity = EntidadViaje::class,
            parentColumns = ["idViaje"],
            childColumns = ["idViaje"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idViaje"), Index("idEmisor"), Index("idReceptor")]
)
data class EntidadMensaje(
    @PrimaryKey(autoGenerate = true)
    val idMensaje: Int = 0,
    val idViaje: Int,
    val idEmisor: String,
    val idReceptor: String,
    val contenido: String,
    val enviadoEn: Long = System.currentTimeMillis(),
    val leido: Boolean = false,
    val sincronizado: Boolean = false  // false = pendiente de enviar al servidor
)