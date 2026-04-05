package com.bedoya.compartrip

import android.app.Application
import androidx.room.Room
import com.bedoya.compartrip.data.local.database.CompartripBaseDatos
import com.bedoya.compartrip.data.local.entity.EntidadUsuario
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class CompartripApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Insertamos un usuario temporal para poder probar sin Auth0
        // Cuando integremos Auth0 esto desaparecerá
        CoroutineScope(Dispatchers.IO).launch {
            val bd = Room.databaseBuilder(
                applicationContext,
                CompartripBaseDatos::class.java,
                "compartrip_bd"
            ).build()

            bd.daoUsuario().insertar(
                EntidadUsuario(
                    idUsuario = "usuario_temporal",
                    nombre = "Nora Bedoya",
                    correo = "nora@compartrip.com"
                )
            )
        }
    }
}