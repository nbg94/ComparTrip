package com.bedoya.compartrip.ui.screens.completarperfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bedoya.compartrip.SesionUsuario
import com.bedoya.compartrip.data.local.entity.EntidadUsuario
import com.bedoya.compartrip.data.repository.RepositorioUsuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EstadoUiCompletarPerfil(
    val nombre: String = "",
    val apellidos: String = "",
    val biografia: String = "",
    val urlFotoLocal: String? = null,
    val errorNombre: String? = null,
    val errorApellidos: String? = null,
    val estaCargando: Boolean = false,
    val error: String? = null,
    val guardadoConExito: Boolean = false
)

@HiltViewModel
class CompletarPerfilViewModel @Inject constructor(
    private val repositorioUsuario: RepositorioUsuario
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoUiCompletarPerfil())
    val estado: StateFlow<EstadoUiCompletarPerfil> = _estado.asStateFlow()

    fun alCambiarNombre(valor: String) =
        _estado.update { it.copy(nombre = valor, errorNombre = null) }

    fun alCambiarApellidos(valor: String) =
        _estado.update { it.copy(apellidos = valor, errorApellidos = null) }

    fun alCambiarBiografia(valor: String) =
        _estado.update { it.copy(biografia = valor) }

    fun alCambiarFoto(uri: String) = _estado.update { it.copy(urlFotoLocal = uri) }

    fun alGuardar() {
        // Validación
        var hayError = false
        if (_estado.value.nombre.isBlank()) {
            _estado.update { it.copy(errorNombre = "El nombre es obligatorio") }
            hayError = true
        }
        if (_estado.value.apellidos.isBlank()) {
            _estado.update { it.copy(errorApellidos = "Los apellidos son obligatorios") }
            hayError = true
        }
        if (hayError) return

        viewModelScope.launch {
            _estado.update { it.copy(estaCargando = true) }
            try {
                val nombreCompleto = "${_estado.value.nombre.trim()} ${_estado.value.apellidos.trim()}"

                // Buscamos si ya existe el usuario en BD para no perder datos
                val usuarioExistente = repositorioUsuario
                    .obtenerUsuario(SesionUsuario.idActual)
                    .first()

                val entidad = EntidadUsuario(
                    idUsuario = SesionUsuario.idActual,
                    nombre = nombreCompleto,
                    correo = usuarioExistente?.correo ?: "",
                    urlFoto = _estado.value.urlFotoLocal ?: usuarioExistente?.urlFoto,
                    biografia = _estado.value.biografia.trim().ifBlank { null },
                    perfilCompleto = true
                )

                repositorioUsuario.guardarUsuario(entidad)

                // Actualizamos la sesión en memoria con el nombre real
                SesionUsuario.nombreActual = nombreCompleto

                _estado.update { it.copy(estaCargando = false, guardadoConExito = true) }
            } catch (e: Exception) {
                _estado.update { it.copy(estaCargando = false, error = e.message) }
            }
        }
    }
}