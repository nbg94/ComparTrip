package com.bedoya.compartrip.ui.screens.mensajes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bedoya.compartrip.SesionUsuario
import com.bedoya.compartrip.data.repository.RepositorioMensaje
import com.bedoya.compartrip.data.repository.RepositorioViaje
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MensajesViewModel @Inject constructor(
    private val repositorioMensaje: RepositorioMensaje,
    private val repositorioViaje: RepositorioViaje
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoUiMensajes())
    val estado: StateFlow<EstadoUiMensajes> = _estado.asStateFlow()

    init {
        cargarConversaciones()
    }

    private fun cargarConversaciones() {
        viewModelScope.launch {
            val idUsuario = SesionUsuario.idActual

            repositorioMensaje.obtenerConversaciones(idUsuario).collect { mensajes ->
                val conversaciones = mensajes
                    .groupBy { it.idViaje }
                    .map { (idViaje, mensajesViaje) ->
                        val ultimo = mensajesViaje.maxByOrNull { it.enviadoEn }!!
                        val noLeidos = mensajesViaje.count {
                            !it.leido && it.idReceptor == idUsuario
                        }
                        val idReceptor = if (ultimo.idEmisor == idUsuario)
                            ultimo.idReceptor else ultimo.idEmisor

                        // Obtenemos el nombre real del viaje
                        val entidadViaje = repositorioViaje
                            .obtenerViajePorId(idViaje).first()
                        val nombreViaje = if (entidadViaje != null)
                            "${entidadViaje.origen} → ${entidadViaje.destino}"
                        else "Viaje #$idViaje"

                        ConversacionUi(
                            idViaje = idViaje,
                            nombreViaje = nombreViaje,
                            idReceptor = idReceptor,
                            nombreReceptor = "Usuario",
                            ultimoMensaje = ultimo.contenido,
                            horaUltimo = ultimo.enviadoEn,
                            noLeidos = noLeidos
                        )
                    }
                    .sortedByDescending { it.horaUltimo }

                _estado.update {
                    it.copy(conversaciones = conversaciones, estaCargando = false)
                }
            }
        }
    }
}