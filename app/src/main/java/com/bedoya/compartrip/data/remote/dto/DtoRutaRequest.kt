package com.bedoya.compartrip.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DtoRutaRequest(
    @SerializedName("coordinates")
    val coordenadas: List<List<Double>>
)