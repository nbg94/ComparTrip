package com.bedoya.compartrip.data.remote.api

import com.bedoya.compartrip.data.remote.dto.DtoViaje
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ServicioApi {

    @GET("posts")
    // @GET → es una llamada HTTP GET a la ruta "posts"
    // la URL completa será: URL_BASE + "posts"
    suspend fun obtenerViajes(): Response<List<DtoViaje>>

    @GET("posts/{id}")
    // {id} es un parámetro dinámico en la URL → posts/1, posts/2, etc.
    suspend fun obtenerViajePorId(
        @Path("id") id: Int   // @Path reemplaza {id} por el valor que le pases
    ): Response<DtoViaje>
}