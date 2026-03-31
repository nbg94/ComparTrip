package com.bedoya.compartrip.di

import com.bedoya.compartrip.data.local.dao.DaoViaje
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
        daoViaje: DaoViaje   // Hilt ya sabe crear DaoViaje (lo definimos en ModuloBaseDatos)
    ): RepositorioViaje {
        return RepositorioViaje(daoViaje)
    }
}