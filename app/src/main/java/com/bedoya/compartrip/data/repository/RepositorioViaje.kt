package com.bedoya.compartrip.data.repository

import com.bedoya.compartrip.data.local.dao.DaoViaje
import com.bedoya.compartrip.data.local.entity.EntidadViaje
import kotlinx.coroutines.flow.Flow

class RepositorioViaje(
    private val daoViaje: DaoViaje  // acceso a la BD local
) {
    // ---- Operaciones locales (Room) ----

    fun obtenerViajesActivos(): Flow<List<EntidadViaje>> =
        daoViaje.obtenerActivos()
    // devuelve un Flow → la pantalla se actualiza sola cuando cambia la BD

    fun buscarViajes(destino: String, tipo: String, modo: String = "DESTINO"): Flow<List<EntidadViaje>> =
        daoViaje.buscar(destino, tipo, modo)

    fun obtenerViajesPorUsuario(idUsuario: String): Flow<List<EntidadViaje>> =
        daoViaje.obtenerPorUsuario(idUsuario)

    fun obtenerViajePorId(idViaje: Int): Flow<EntidadViaje?> =
        daoViaje.obtenerPorId(idViaje)

    suspend fun guardarViaje(viaje: EntidadViaje) =
        daoViaje.insertar(viaje)

    suspend fun actualizarViaje(viaje: EntidadViaje) =
        daoViaje.actualizar(viaje)

    suspend fun eliminarViaje(viaje: EntidadViaje) =
        daoViaje.eliminar(viaje)

    // ---- Operaciones remotas (Retrofit) ----



}
