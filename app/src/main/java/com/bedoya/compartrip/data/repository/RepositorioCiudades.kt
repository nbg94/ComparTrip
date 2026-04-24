package com.bedoya.compartrip.data.repository

import com.bedoya.compartrip.data.remote.api.ServicioApi
import com.bedoya.compartrip.data.remote.dto.DtoCiudad

class RepositorioCiudades(
    private val servicioApi: ServicioApi
) {
    suspend fun buscarCiudades(texto: String): Result<List<DtoCiudad>> {
        if (texto.length < 2) return Result.success(emptyList())
        // No buscamos si hay menos de 2 caracteres
        return try {
            val respuesta = servicioApi.buscarCiudades(texto)
            if (respuesta.isSuccessful && respuesta.body() != null) {
                Result.success(respuesta.body()!!)
            } else {
                Result.failure(Exception("Error buscando ciudades"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}