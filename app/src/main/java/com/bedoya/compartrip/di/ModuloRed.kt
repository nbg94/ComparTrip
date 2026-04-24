package com.bedoya.compartrip.di

import com.bedoya.compartrip.data.remote.api.ServicioApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuloRed {

    @Provides
    @Singleton
    fun proveerOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "Compartrip/1.0 (Android)")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    @Named("nominatim")
    fun proveerRetrofitNominatim(clienteHttp: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named("ors")
    fun proveerRetrofitORS(clienteHttp: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org/")
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named("servicioNominatim")
    fun proveerServicioNominatim(@Named("nominatim") retrofit: Retrofit): ServicioApi =
        retrofit.create(ServicioApi::class.java)

    @Provides
    @Singleton
    @Named("servicioORS")
    fun proveerServicioORS(@Named("ors") retrofit: Retrofit): ServicioApi =
        retrofit.create(ServicioApi::class.java)
}