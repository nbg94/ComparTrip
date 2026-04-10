package com.bedoya.compartrip

// Guarda el estado de sesión en memoria mientras la app está abierta
// Cuando integremos una solución más robusta (DataStore) lo mejoraremos
object SesionUsuario {
    var idActual: String = "usuario_temporal"
    var nombreActual: String = ""
    var urlFotoActual: String? = null
}