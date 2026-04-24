package com.bedoya.compartrip.di

import com.bedoya.compartrip.data.local.dao.DaoUsuario
import com.bedoya.compartrip.data.local.dao.DaoViaje
import com.bedoya.compartrip.data.remote.api.ServicioApi
import com.bedoya.compartrip.data.repository.RepositorioCiudades
import com.bedoya.compartrip.data.repository.RepositorioUsuario
import com.bedoya.compartrip.data.repository.RepositorioViaje
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuloRepositorio {

    @Provides
    @Singleton
    fun proveerRepositorioViaje(
        daoViaje: DaoViaje
    ): RepositorioViaje = RepositorioViaje(daoViaje)

    @Provides
    @Singleton
    fun proveerRepositorioUsuario(
        daoUsuario: DaoUsuario
    ): RepositorioUsuario = RepositorioUsuario(daoUsuario)

    @Provides
    @Singleton
    fun proveerRepositorioCiudades(
        servicioApi: ServicioApi
    ): RepositorioCiudades = RepositorioCiudades(servicioApi)

}