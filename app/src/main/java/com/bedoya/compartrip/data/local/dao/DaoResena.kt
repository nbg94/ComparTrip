package com.bedoya.compartrip.data.local.dao

import androidx.room.*
import com.bedoya.compartrip.data.local.entity.EntidadResena
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoResena {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(resena: EntidadResena)

    @Update
    suspend fun actualizar(resena: EntidadResena)

    @Delete
    suspend fun eliminar(resena: EntidadResena)

    @Query("SELECT * FROM resenas WHERE idResenado = :idUsuario ORDER BY creadoEn DESC")
    fun obtenerResenasPorUsuario(idUsuario: String): Flow<List<EntidadResena>>

    @Query("SELECT AVG(puntuacion) FROM resenas WHERE idResenado = :idUsuario")
    fun obtenerPuntuacionMedia(idUsuario: String): Flow<Float?>
    // AVG = media aritmética de todas las puntuaciones recibidas
}