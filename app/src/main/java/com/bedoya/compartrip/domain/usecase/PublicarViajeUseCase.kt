package com.bedoya.compartrip.domain.usecase

import com.bedoya.compartrip.data.repository.RepositorioViaje
import com.bedoya.compartrip.domain.model.Viaje
import com.bedoya.compartrip.domain.model.aEntidad

class PublicarViajeUseCase(
    private val repositorio: RepositorioViaje
) {
    suspend fun ejecutar(viaje: Viaje) {
        // validación básica antes de guardar
        require(viaje.origen.isNotBlank()) { "El origen no puede estar vacío" }
        require(viaje.destino.isNotBlank()) { "El destino no puede estar vacío" }
        require(viaje.plazasDisponibles > 0) { "Debe haber al menos una plaza disponible" }
        // require → si la condición es falsa lanza una excepción con el mensaje indicado

        repositorio.guardarViaje(viaje.aEntidad())
    }
}