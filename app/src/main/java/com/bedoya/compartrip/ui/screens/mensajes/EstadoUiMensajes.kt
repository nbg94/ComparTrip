package com.bedoya.compartrip.ui.screens.mensajes

data class ConversacionUi(
    val idViaje: Int,
    val nombreViaje: String,
    val idReceptor: String,
    val nombreReceptor: String,
    val ultimoMensaje: String,
    val horaUltimo: Long,
    val noLeidos: Int = 0
)

data class EstadoUiMensajes(
    val conversaciones: List<ConversacionUi> = emptyList(),
    val estaCargando: Boolean = true
)