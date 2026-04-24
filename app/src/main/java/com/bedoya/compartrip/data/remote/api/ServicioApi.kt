package com.bedoya.compartrip.data.remote.api

import com.bedoya.compartrip.data.remote.dto.DtoCiudad
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ServicioApi {

    // ---- Nominatim (OpenStreetMap) — autocompletado de ciudades ----
    @GET("search")
    suspend fun buscarCiudades(
        @Query("q") texto: String,
        @Query("format") formato: String = "json",
        @Query("addressdetails") detalles: Int = 1,
        @Query("limit") limite: Int = 5,
        // limit = 5 → máximo 5 sugerencias
        @Query("featuretype") tipo: String = "city",
        // featuretype = city → solo ciudades, no calles ni edificios
        @Query("accept-language") idioma: String = "es"
    ): Response<List<DtoCiudad>>
}