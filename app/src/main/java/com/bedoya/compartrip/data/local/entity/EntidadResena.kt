package com.bedoya.compartrip.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "resenas",
    foreignKeys = [
        ForeignKey(
            entity = EntidadUsuario::class,
            parentColumns = ["idUsuario"],
            childColumns = ["idResenador"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EntidadUsuario::class,
            parentColumns = ["idUsuario"],
            childColumns = ["idResenado"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idResenador"), Index("idResenado")]
)
data class EntidadResena(
    @PrimaryKey(autoGenerate = true)
    val idResena: Int = 0,
    val idReserva: Int,
    val idResenador: String,           // usuario que escribe la reseña
    val idResenado: String,            // usuario que la recibe
    val puntuacion: Int,               // del 1 al 5
    val comentario: String? = null,
    val creadoEn: Long = System.currentTimeMillis()
)