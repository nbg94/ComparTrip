package com.bedoya.compartrip.ui.screens.misviajes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bedoya.compartrip.SesionUsuario
import com.bedoya.compartrip.data.local.entity.EntidadReserva
import com.bedoya.compartrip.data.repository.RepositorioReserva
import com.bedoya.compartrip.data.repository.RepositorioViaje
import com.bedoya.compartrip.domain.model.Viaje
import com.bedoya.compartrip.domain.model.aDominio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MisViajesViewModel @Inject constructor(
    private val repositorioViaje: RepositorioViaje,
    private val repositorioReserva: RepositorioReserva
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoUiMisViajes())
    val estado: StateFlow<EstadoUiMisViajes> = _estado.asStateFlow()

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            val idUsuario = SesionUsuario.idActual

            // Combinamos los tres flows en uno solo
            combine(
                repositorioViaje.obtenerViajesPorUsuario(idUsuario),
                repositorioReserva.obtenerPorUsuario(idUsuario),
                repositorioReserva.obtenerSolicitudesDeViajes(idUsuario)
            ) { viajesPublicados, misReservas, solicitudesRecibidas ->
                Triple(viajesPublicados, misReservas, solicitudesRecibidas)
            }.collect { (viajesPublicados, misReservas, solicitudesRecibidas) ->
                _estado.update {
                    it.copy(
                        viajesPublicados = viajesPublicados.map { e -> e.aDominio() },
                        solicitudesRecibidas = solicitudesRecibidas,
                        estaCargando = false
                    )
                }
            }
        }

        // Cargamos los viajes en los que participa (como solicitante aceptado)
        viewModelScope.launch {
            repositorioReserva.obtenerPorUsuario(SesionUsuario.idActual).collect { reservas ->
                val idsViajes = reservas
                    .filter { it.estado == "ACEPTADA" }
                    .map { it.idViaje }

                val viajes = mutableListOf<Viaje>()
                idsViajes.forEach { idViaje ->
                    repositorioViaje.obtenerViajePorId(idViaje).collect { entidad ->
                        entidad?.let { viajes.add(it.aDominio()) }
                    }
                }
                _estado.update { it.copy(viajesParticipando = viajes) }
            }
        }
    }

    fun alCambiarPestana(indice: Int) =
        _estado.update { it.copy(pestanaSeleccionada = indice) }

    fun aceptarSolicitud(reserva: EntidadReserva) {
        viewModelScope.launch {
            repositorioReserva.actualizarEstado(
                reserva.copy(
                    estado = "ACEPTADA",
                    actualizadoEn = System.currentTimeMillis()
                )
            )
        }
    }

    fun rechazarSolicitud(reserva: EntidadReserva) {
        viewModelScope.launch {
            repositorioReserva.actualizarEstado(
                reserva.copy(
                    estado = "RECHAZADA",
                    actualizadoEn = System.currentTimeMillis()
                )
            )
        }
    }
}