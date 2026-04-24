package com.bedoya.compartrip.data.repository

import com.bedoya.compartrip.data.local.dao.DaoUsuario
import com.bedoya.compartrip.data.local.entity.EntidadUsuario
import kotlinx.coroutines.flow.Flow

class RepositorioUsuario(
    private val daoUsuario: DaoUsuario
) {
    suspend fun guardarUsuario(usuario: EntidadUsuario) =
        daoUsuario.insertar(usuario)

    fun obtenerUsuario(id: String): Flow<EntidadUsuario?> =
        daoUsuario.obtenerPorId(id)

    suspend fun actualizarUsuario(usuario: EntidadUsuario) =
        daoUsuario.actualizar(usuario)
}

