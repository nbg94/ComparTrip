package com.bedoya.compartrip.data.local.dao

import androidx.room.*
import com.bedoya.compartrip.data.local.entity.EntidadUsuario
import kotlinx.coroutines.flow.Flow

@Dao  // le dice a Room que esta interfaz es un DAO
interface DaoUsuario {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: EntidadUsuario)
    // suspend = función asíncrona (no bloquea la pantalla mientras guarda)

    @Update
    suspend fun actualizar(usuario: EntidadUsuario)

    @Delete
    suspend fun eliminar(usuario: EntidadUsuario)

    @Query("SELECT * FROM usuarios WHERE idUsuario = :id")
    fun obtenerPorId(id: String): Flow<EntidadUsuario?>
    // Flow = se actualiza automáticamente cuando cambian los datos en BD

    @Query("SELECT * FROM usuarios")
    fun obtenerTodos(): Flow<List<EntidadUsuario>>
}