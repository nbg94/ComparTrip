package com.bedoya.compartrip.data.repository

import com.bedoya.compartrip.data.local.dao.DaoMensaje
import com.bedoya.compartrip.data.local.entity.EntidadMensaje
import kotlinx.coroutines.flow.Flow

class RepositorioMensaje(
    private val daoMensaje: DaoMensaje
) {
    suspend fun insertar(mensaje: EntidadMensaje) =
        daoMensaje.insertar(mensaje)

    fun obtenerMensajesDeViaje(idViaje: Int): Flow<List<EntidadMensaje>> =
        daoMensaje.obtenerMensajesDeViaje(idViaje)

    suspend fun marcarComoLeidos(idViaje: Int, idUsuario: String) =
        daoMensaje.marcarComoLeidos(idViaje, idUsuario)

    fun obtenerConversaciones(idUsuario: String): Flow<List<EntidadMensaje>> =
        daoMensaje.obtenerConversacionesDeUsuario(idUsuario)
}