package com.bedoya.compartrip.di

import com.bedoya.compartrip.data.repository.RepositorioViaje
import com.bedoya.compartrip.domain.usecase.BuscarViajesUseCase
import com.bedoya.compartrip.domain.usecase.ObtenerViajesUseCase
import com.bedoya.compartrip.domain.usecase.PublicarViajeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuloCasosDeUso {

    @Provides
    @Singleton
    fun proveerObtenerViajesUseCase(
        repositorio: RepositorioViaje  // Hilt lo inyecta solo
    ): ObtenerViajesUseCase = ObtenerViajesUseCase(repositorio)

    @Provides
    @Singleton
    fun proveerPublicarViajeUseCase(
        repositorio: RepositorioViaje
    ): PublicarViajeUseCase = PublicarViajeUseCase(repositorio)

    @Provides
    @Singleton
    fun proveerBuscarViajesUseCase(
        repositorio: RepositorioViaje
    ): BuscarViajesUseCase = BuscarViajesUseCase(repositorio)
}