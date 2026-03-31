package com.bedoya.compartrip.data.local.dao

import androidx.room.*
import com.bedoya.compartrip.data.local.entity.EntidadReserva
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoReserva {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(reserva: EntidadReserva)

    @Update
    suspend fun actualizar(reserva: EntidadReserva)

    @Delete
    suspend fun eliminar(reserva: EntidadReserva)

    @Query("SELECT * FROM reservas WHERE idViaje = :idViaje")
    fun obtenerPorViaje(idViaje: Int): Flow<List<EntidadReserva>>

    @Query("SELECT * FROM reservas WHERE idUsuario = :idUsuario")
    fun obtenerPorUsuario(idUsuario: String): Flow<List<EntidadReserva>>

    @Query("SELECT * FROM reservas WHERE idViaje = :idViaje AND idUsuario = :idUsuario LIMIT 1")
    fun obtenerReservaConcreta(idViaje: Int, idUsuario: String): Flow<EntidadReserva?>
    // LIMIT 1 = solo devuelve un resultado (un usuario solo puede tener una reserva por viaje)
}