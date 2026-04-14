package com.bedoya.compartrip.di

import android.content.Context
import androidx.room.Room
import com.bedoya.compartrip.data.local.dao.*
import com.bedoya.compartrip.data.local.database.CompartripBaseDatos
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module                          // le dice a Hilt que aquí hay instrucciones de creación
@InstallIn(SingletonComponent::class)  // estos objetos vivirán toda la vida de la app
object ModuloBaseDatos {

    @Provides                    // @Provides = "así se crea este objeto"
    @Singleton                   // @Singleton = solo se crea una vez en toda la app
    fun proveerBaseDatos(
        @ApplicationContext contexto: Context
    ): CompartripBaseDatos {
        return Room.databaseBuilder(
            contexto,
            CompartripBaseDatos::class.java,
            "compartrip_bd"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun proveerDaoViaje(bd: CompartripBaseDatos): DaoViaje = bd.daoViaje()
    // Hilt ya sabe crear CompartripBaseDatos (la definimos arriba), así que la inyecta sola

    @Provides
    @Singleton
    fun proveerDaoUsuario(bd: CompartripBaseDatos): DaoUsuario = bd.daoUsuario()

    @Provides
    @Singleton
    fun proveerDaoReserva(bd: CompartripBaseDatos): DaoReserva = bd.daoReserva()

    @Provides
    @Singleton
    fun proveerDaoResena(bd: CompartripBaseDatos): DaoResena = bd.daoResena()

    @Provides
    @Singleton
    fun proveerDaoMensaje(bd: CompartripBaseDatos): DaoMensaje = bd.daoMensaje()
}