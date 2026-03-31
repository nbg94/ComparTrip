package com.bedoya.compartrip.domain.usecase

import com.bedoya.compartrip.data.repository.RepositorioViaje
import com.bedoya.compartrip.domain.model.Viaje
import com.bedoya.compartrip.domain.model.aDominio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObtenerViajesUseCase(
    private val repositorio: RepositorioViaje
) {
    // map → transforma cada EntidadViaje de la lista en un Viaje de dominio
    fun ejecutar(): Flow<List<Viaje>> =
        repositorio.obtenerViajesActivos()
            .map { lista -> lista.map { it.aDominio() } }
}