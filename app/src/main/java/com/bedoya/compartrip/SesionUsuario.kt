package com.bedoya.compartrip

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SesionUsuario {
    // StateFlow para que los observadores reaccionen cuando cambia
    private val _idActual = MutableStateFlow("")
    val idActualFlow: StateFlow<String> = _idActual.asStateFlow()

    // Acceso rápido para código no-reactivo (igual que antes)
    var idActual: String
        get() = _idActual.value
        set(value) { _idActual.value = value }

    var nombreActual: String = ""
    var urlFotoActual: String = ""
}