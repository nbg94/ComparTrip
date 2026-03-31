package com.bedoya.compartrip.ui.navigation

// Cada objeto es una ruta de navegación de la app
// Usamos object para que sean singleton y no se puedan instanciar
sealed class Destinos(val ruta: String) {
    object Login : Destinos("login")
    object Home : Destinos("home")
    object DetalleViaje : Destinos("detalle/{idViaje}") {
        // función helper para construir la ruta con el ID real
        fun crearRuta(idViaje: Int) = "detalle/$idViaje"
    }
    object PublicarViaje : Destinos("publicar")
    object Perfil : Destinos("perfil")
    object Mensajes : Destinos("mensajes")
}