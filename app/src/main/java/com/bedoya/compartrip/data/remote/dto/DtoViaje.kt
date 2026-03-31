package com.bedoya.compartrip.data.remote.dto

import com.google.gson.annotations.SerializedName

// Esta clase representa el JSON que nos devuelve la API
// @SerializedName mapea el nombre del campo JSON al nombre de tu variable Kotlin
data class DtoViaje(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val titulo: String,

    @SerializedName("body")
    val descripcion: String,

    @SerializedName("userId")
    val idUsuario: Int
)
// JSONPlaceholder devuelve "posts" con estos campos
// Más adelante cuando tengas tu propio backend, este DTO tendrá origen, destino, fecha...