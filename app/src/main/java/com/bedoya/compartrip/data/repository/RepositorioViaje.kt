package com.bedoya.compartrip.data.repository

import com.bedoya.compartrip.data.local.dao.DaoViaje
import com.bedoya.compartrip.data.local.entity.EntidadViaje
import com.bedoya.compartrip.data.remote.api.ClienteRetrofit
import com.bedoya.compartrip.data.remote.dto.DtoViaje
import kotlinx.coroutines.flow.Flow

class RepositorioViaje(
    private val daoViaje: DaoViaje  // acceso a la BD local
) {
    // ---- Operaciones locales (Room) ----

    fun obtenerViajesActivos(): Flow<List<EntidadViaje>> =
        daoViaje.obtenerActivos()
    // devuelve un Flow → la pantalla se actualiza sola cuando cambia la BD

    fun buscarViajes(destino: String, tipo: String): Flow<List<EntidadViaje>> =
        daoViaje.buscar(destino, tipo)

    fun obtenerViajesPorUsuario(idUsuario: String): Flow<List<EntidadViaje>> =
        daoViaje.obtenerPorUsuario(idUsuario)

    suspend fun guardarViaje(viaje: EntidadViaje) =
        daoViaje.insertar(viaje)

    suspend fun actualizarViaje(viaje: EntidadViaje) =
        daoViaje.actualizar(viaje)

    suspend fun eliminarViaje(viaje: EntidadViaje) =
        daoViaje.eliminar(viaje)

    // ---- Operaciones remotas (Retrofit) ----

    suspend fun obtenerViajesDeApi(): Result<List<DtoViaje>> {
        return try {
            val respuesta = ClienteRetrofit.instancia.obtenerViajes()
            if (respuesta.isSuccessful && respuesta.body() != null) {
                Result.success(respuesta.body()!!)
                // Result.success → la llamada fue bien, devolvemos los datos
            } else {
                Result.failure(Exception("Error ${respuesta.code()}: ${respuesta.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
            // Result.failure → algo salió mal (sin internet, timeout, etc.)
        }
    }
}
