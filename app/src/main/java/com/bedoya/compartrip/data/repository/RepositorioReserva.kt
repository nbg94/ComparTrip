package com.bedoya.compartrip.data.repository

import com.bedoya.compartrip.data.local.dao.DaoReserva
import com.bedoya.compartrip.data.local.entity.EntidadReserva
import kotlinx.coroutines.flow.Flow

class RepositorioReserva(
    private val daoReserva: DaoReserva
) {
    suspend fun insertar(reserva: EntidadReserva) =
        daoReserva.insertar(reserva)

    fun obtenerPorUsuario(idUsuario: String): Flow<List<EntidadReserva>> =
        daoReserva.obtenerPorUsuario(idUsuario)

    fun obtenerPorViaje(idViaje: Int): Flow<List<EntidadReserva>> =
        daoReserva.obtenerPorViaje(idViaje)

    fun obtenerReservaConcreta(idViaje: Int, idUsuario: String): Flow<EntidadReserva?> =
        daoReserva.obtenerReservaConcreta(idViaje, idUsuario)
}