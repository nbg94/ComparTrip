package com.bedoya.compartrip.domain.usecase

import com.bedoya.compartrip.data.local.entity.EntidadReserva
import com.bedoya.compartrip.data.repository.RepositorioReserva

class SolicitarViajeUseCase(
    private val repositorio: RepositorioReserva
) {
    suspend fun ejecutar(idViaje: Int, idUsuario: String): Result<Unit> {
        return try {
            require(idUsuario.isNotBlank()) { "Usuario no autenticado" }
            val reserva = EntidadReserva(
                idViaje = idViaje,
                idUsuario = idUsuario,
                estado = "PENDIENTE"
            )
            repositorio.insertar(reserva)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}