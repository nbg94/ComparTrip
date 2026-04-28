package com.bedoya.compartrip.data.local.dao

import androidx.room.*
import com.bedoya.compartrip.data.local.entity.EntidadMensaje
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoMensaje {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(mensaje: EntidadMensaje)

    @Update
    suspend fun actualizar(mensaje: EntidadMensaje)

    @Delete
    suspend fun eliminar(mensaje: EntidadMensaje)

    @Query("SELECT * FROM mensajes WHERE idViaje = :idViaje ORDER BY enviadoEn ASC")
    fun obtenerMensajesDeViaje(idViaje: Int): Flow<List<EntidadMensaje>>
    // ASC = del más antiguo al más nuevo (orden natural de un chat)

    @Query("SELECT * FROM mensajes WHERE sincronizado = 0")
    suspend fun obtenerNoSincronizados(): List<EntidadMensaje>
    // estos son los mensajes guardados offline que aún no se enviaron al servidor

    @Query("UPDATE mensajes SET leido = 1 WHERE idViaje = :idViaje AND idReceptor = :idUsuario")
    suspend fun marcarComoLeidos(idViaje: Int, idUsuario: String)

    @Query("""
    SELECT * FROM mensajes 
    WHERE idEmisor = :idUsuario OR idReceptor = :idUsuario
    ORDER BY enviadoEn DESC
""")
    fun obtenerConversacionesDeUsuario(idUsuario: String): Flow<List<EntidadMensaje>>
}