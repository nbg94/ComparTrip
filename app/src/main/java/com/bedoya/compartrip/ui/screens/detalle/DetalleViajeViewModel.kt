package com.bedoya.compartrip.ui.screens.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bedoya.compartrip.data.repository.RepositorioUsuario
import com.bedoya.compartrip.data.repository.RepositorioViaje
import com.bedoya.compartrip.domain.model.aDominio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalleViajeViewModel @Inject constructor(
    private val repositorioViaje: RepositorioViaje,
    private val repositorioUsuario: RepositorioUsuario
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoUiDetalle())
    val estado: StateFlow<EstadoUiDetalle> = _estado.asStateFlow()

    fun cargarViaje(idViaje: Int) {
        viewModelScope.launch {
            // Recogemos el viaje de Room por su ID
            repositorioViaje.obtenerViajePorId(idViaje).collect { entidad ->
                if (entidad == null) {
                    _estado.update { it.copy(estaCargando = false, error = "Viaje no encontrado") }
                    return@collect
                }

                val viaje = entidad.aDominio()
                _estado.update { it.copy(viaje = viaje, estaCargando = false) }

                // Cargamos también el perfil del publicador
                repositorioUsuario.obtenerUsuario(viaje.idPublicador).collect { usuarioEntidad ->
                    val publicador = usuarioEntidad?.aDominio()
                    _estado.update { it.copy(publicador = publicador) }
                }
            }
        }
    }
}