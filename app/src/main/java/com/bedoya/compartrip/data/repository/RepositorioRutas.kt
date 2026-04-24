package com.bedoya.compartrip.data.repository

import com.bedoya.compartrip.data.remote.api.ServicioApi
import com.bedoya.compartrip.data.remote.dto.DtoRuta
import com.bedoya.compartrip.data.remote.dto.DtoRutaRequest

class RepositorioRutas(
    private val servicioApi: ServicioApi
) {
    suspend fun calcularRuta(
        origenLat: Double, origenLon: Double,
        destinoLat: Double, destinoLon: Double,
        apiKey: String
    ): Result<DtoRuta> {
        return try {
            val cuerpo = DtoRutaRequest(
                coordenadas = listOf(
                    listOf(origenLon, origenLat),   // ORS usa [lon, lat]
                    listOf(destinoLon, destinoLat)
                )
            )
            val respuesta = servicioApi.calcularRuta(apiKey, cuerpo)
            if (respuesta.isSuccessful && respuesta.body() != null) {
                Result.success(respuesta.body()!!)
            } else {
                android.util.Log.e("RUTA", "Error ORS HTTP: ${respuesta.code()} ${respuesta.errorBody()?.string()}")
                Result.failure(Exception("Error calculando ruta: ${respuesta.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}