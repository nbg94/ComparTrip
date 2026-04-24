package com.bedoya.compartrip.data.remote.api

import com.bedoya.compartrip.data.remote.dto.DtoCiudad
import com.bedoya.compartrip.data.remote.dto.DtoRuta
import com.bedoya.compartrip.data.remote.dto.DtoRutaRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ServicioApi {

    // ---- Nominatim — autocompletado de ciudades ----
    @GET("search")
    suspend fun buscarCiudades(
        @Query("q") texto: String,
        @Query("format") formato: String = "json",
        @Query("addressdetails") detalles: Int = 1,
        @Query("limit") limite: Int = 5,
        @Query("featuretype") tipo: String = "city",
        @Query("accept-language") idioma: String = "es"
    ): Response<List<DtoCiudad>>

    // ---- OpenRouteService — distancia y duración ----
    @POST("v2/directions/driving-car")
    suspend fun calcularRuta(
        @Header("Authorization") apiKey: String,
        @Body cuerpo: DtoRutaRequest
    ): Response<DtoRuta>
}