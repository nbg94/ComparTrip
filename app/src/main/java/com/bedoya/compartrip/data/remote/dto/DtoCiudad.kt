package com.bedoya.compartrip.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DtoCiudad(
    @SerializedName("display_name")
    val nombreCompleto: String,

    @SerializedName("name")
    val nombre: String? = null,

    @SerializedName("lat")
    val latitud: String,

    @SerializedName("lon")
    val longitud: String,

    @SerializedName("address")
    val direccion: DtoDireccion? = null
)

data class DtoDireccion(
    @SerializedName("city")
    val ciudad: String? = null,

    @SerializedName("town")
    val pueblo: String? = null,

    @SerializedName("village")
    val aldea: String? = null,

    @SerializedName("country")
    val pais: String? = null,

    @SerializedName("state")
    val region: String? = null
)