package com.bedoya.compartrip.domain.usecase

import com.bedoya.compartrip.data.repository.RepositorioViaje
import com.bedoya.compartrip.domain.model.Viaje
import com.bedoya.compartrip.domain.model.aDominio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BuscarViajesUseCase(
    private val repositorio: RepositorioViaje
) {
    fun ejecutar(texto: String = "", tipo: String = "", modo: String = "DESTINO"): Flow<List<Viaje>> =
        repositorio.buscarViajes(texto, tipo, modo)
            .map { lista -> lista.map { it.aDominio() } }
}