package com.bedoya.compartrip.data.remote.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClienteRetrofit {
    // URL base de la API — todo lo que definas en ServicioApi se añade a esta URL
    private const val URL_BASE = "https://jsonplaceholder.typicode.com/"

    // Interceptor de logs → muestra en Logcat todas las llamadas HTTP que hace la app
    // Muy útil para depurar. En producción se quitaría.
    private val interceptorLogs = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
        // BODY → muestra URL, cabeceras y cuerpo completo de cada petición/respuesta
    }

    // OkHttpClient es el motor HTTP que usa Retrofit por debajo
    private val clienteHttp = OkHttpClient.Builder()
        .addInterceptor(interceptorLogs)
        .build()

    // Instancia única de Retrofit (mismo patrón Singleton que la BD)
    val instancia: ServicioApi by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            // GsonConverterFactory → convierte automáticamente el JSON en tus DTOs
            .build()
            .create(ServicioApi::class.java)
    }
}