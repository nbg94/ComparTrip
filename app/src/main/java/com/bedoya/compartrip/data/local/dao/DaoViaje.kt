package com.bedoya.compartrip.data.local.dao

import androidx.room.*
import com.bedoya.compartrip.data.local.entity.EntidadViaje
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoViaje {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(viaje: EntidadViaje)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarVarios(viajes: List<EntidadViaje>)

    @Update
    suspend fun actualizar(viaje: EntidadViaje)

    @Delete
    suspend fun eliminar(viaje: EntidadViaje)

    @Query("SELECT * FROM viajes WHERE idViaje = :id")
    fun obtenerPorId(id: Int): Flow<EntidadViaje?>

    @Query("SELECT * FROM viajes WHERE estado = 'ACTIVO' ORDER BY creadoEn DESC")
    fun obtenerActivos(): Flow<List<EntidadViaje>>
    // ORDER BY creadoEn DESC = los más recientes primero

    @Query("SELECT * FROM viajes WHERE idPublicador = :idUsuario ORDER BY creadoEn DESC")
    fun obtenerPorUsuario(idUsuario: String): Flow<List<EntidadViaje>>

    @Query("""
        SELECT * FROM viajes 
        WHERE estado = 'ACTIVO'
        AND (:destino = '' OR destino LIKE '%' || :destino || '%')
        AND (:tipo = '' OR tipo = :tipo)
        ORDER BY creadoEn DESC
    """)
    fun buscar(destino: String, tipo: String): Flow<List<EntidadViaje>>
    // busca viajes filtrando por destino y tipo, si están vacíos devuelve todos
}