package com.bedoya.compartrip.ui.navigation

sealed class Destinos(val ruta: String) {
    object Login : Destinos("login")
    object CompletarPerfil : Destinos("completar_perfil")
    object Home : Destinos("home")
    object Favoritos : Destinos("favoritos")
    object MisViajes : Destinos("mis_viajes")
    object Mensajes : Destinos("mensajes")
    object Perfil : Destinos("perfil")
    object DetalleViaje : Destinos("detalle/{idViaje}") {
        fun crearRuta(idViaje: Int) = "detalle/$idViaje"
    }
    object PublicarViaje : Destinos("publicar")
    object Chat : Destinos("chat/{idViaje}/{idReceptor}") {
        fun crearRuta(idViaje: Int, idReceptor: String) = "chat/$idViaje/$idReceptor"
    }
}