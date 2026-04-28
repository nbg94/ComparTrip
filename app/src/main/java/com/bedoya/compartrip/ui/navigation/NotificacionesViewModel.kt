package com.bedoya.compartrip.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bedoya.compartrip.SesionUsuario
import com.bedoya.compartrip.data.repository.RepositorioMensaje
import com.bedoya.compartrip.data.repository.RepositorioReserva
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificacionesViewModel @Inject constructor(
    private val repositorioMensaje: RepositorioMensaje,
    private val repositorioReserva: RepositorioReserva
) : ViewModel() {

    private val _mensajesNoLeidos = MutableStateFlow(0)
    val mensajesNoLeidos: StateFlow<Int> = _mensajesNoLeidos.asStateFlow()

    private val _solicitudesPendientes = MutableStateFlow(0)
    val solicitudesPendientes: StateFlow<Int> = _solicitudesPendientes.asStateFlow()

    init {
        observarMensajes()
        observarSolicitudes()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observarMensajes() {
        viewModelScope.launch {
            // Cada vez que cambia el idActual (tras el login), re-lanza la consulta
            SesionUsuario.idActualFlow
                .flatMapLatest { idUsuario ->
                    repositorioMensaje.obtenerConversaciones(idUsuario)
                        .map { mensajes ->
                            mensajes.count { !it.leido && it.idReceptor == idUsuario }
                        }
                }
                .collect { cantidad ->
                    _mensajesNoLeidos.value = cantidad
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observarSolicitudes() {
        viewModelScope.launch {
            SesionUsuario.idActualFlow
                .flatMapLatest { idUsuario ->
                    repositorioReserva.obtenerSolicitudesDeViajes(idUsuario)
                        .map { solicitudes ->
                            solicitudes.count { it.estado == "PENDIENTE" }
                        }
                }
                .collect { cantidad ->
                    _solicitudesPendientes.value = cantidad
                }
        }
    }
}