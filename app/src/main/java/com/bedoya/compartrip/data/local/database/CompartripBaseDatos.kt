package com.bedoya.compartrip.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.bedoya.compartrip.data.local.dao.*
import com.bedoya.compartrip.data.local.entity.*

@Database(
    entities = [
        EntidadUsuario::class,
        EntidadViaje::class,
        EntidadReserva::class,
        EntidadResena::class,
        EntidadMensaje::class
    ],
    version = 3,
    exportSchema = false
    // exportSchema = false → no genera fichero JSON del esquema (lo ponemos a true en producción real)
)
abstract class CompartripBaseDatos : RoomDatabase() {
    // Room genera estas implementaciones automáticamente al compilar
    abstract fun daoUsuario(): DaoUsuario
    abstract fun daoViaje(): DaoViaje
    abstract fun daoReserva(): DaoReserva
    abstract fun daoResena(): DaoResena
    abstract fun daoMensaje(): DaoMensaje

    companion object {
        // @Volatile → cualquier cambio en esta variable es visible inmediatamente para todos los hilos
        @Volatile
        private var INSTANCIA: CompartripBaseDatos? = null

        fun obtenerInstancia(contexto: Context): CompartripBaseDatos {
            // synchronized → evita que dos hilos creen la BD al mismo tiempo
            return INSTANCIA ?: synchronized(this) {
                Room.databaseBuilder(
                    contexto.applicationContext,
                    CompartripBaseDatos::class.java,
                    "compartrip_bd"         // nombre del archivo de BD en el dispositivo
                ).build().also { INSTANCIA = it }
            }
        }
    }
}
