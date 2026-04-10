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
class CompartripApp : Application()