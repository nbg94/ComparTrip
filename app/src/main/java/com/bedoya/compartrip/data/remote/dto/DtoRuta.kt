package com.bedoya.compartrip.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DtoRuta(
    @SerializedName("routes")
    val rutas: List<DtoRutaDetalle>
)

data class DtoRutaDetalle(
    @SerializedName("summary")
    val resumen: DtoRutaResumen
)

data class DtoRutaResumen(
    @SerializedName("distance")
    val distanciaMetros: Double,    // viene en metros

    @SerializedName("duration")
    val duracionSegundos: Double    // viene en segundos
)