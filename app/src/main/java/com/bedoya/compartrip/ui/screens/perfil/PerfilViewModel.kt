package com.bedoya.compartrip.ui.screens.perfil

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.bedoya.compartrip.ConfiguracionAuth0
import com.bedoya.compartrip.SesionUsuario
import com.bedoya.compartrip.data.repository.RepositorioUsuario
import com.bedoya.compartrip.data.repository.RepositorioViaje
import com.bedoya.compartrip.domain.model.Resena
import com.bedoya.compartrip.domain.model.Usuario
import com.bedoya.compartrip.domain.model.aDominio
import com.bedoya.compartrip.domain.model.aEntidad
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EstadoUiPerfil(
    val usuario: Usuario? = null,
    val totalViajesPublicados: Int = 0,
    val resenas: List<Resena> = emptyList(),
    val estaCargando: Boolean = true,
    val error: String? = null,
    val sesionCerrada: Boolean = false,
    val mostrarDialogoVerificacion: Boolean = false,
    val mostrarDialogoBiografia: Boolean = false
)

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val repositorioUsuario: RepositorioUsuario,
    private val repositorioViaje: RepositorioViaje
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoUiPerfil())
    val estado: StateFlow<EstadoUiPerfil> = _estado.asStateFlow()

    private val cuentaAuth0 = Auth0(
        ConfiguracionAuth0.CLIENT_ID,
        ConfiguracionAuth0.DOMINIO
    )

    init {
        cargarPerfil()
    }

    private fun cargarPerfil() {
        viewModelScope.launch {
            val idUsuario = SesionUsuario.idActual
            repositorioUsuario.obtenerUsuario(idUsuario).collect { entidad ->
                if (entidad != null) {
                    _estado.update {
                        it.copy(usuario = entidad.aDominio(), estaCargando = false)
                    }
                }
            }
        }
        viewModelScope.launch {
            // Contamos los viajes publicados por este usuario
            repositorioViaje.obtenerViajesPorUsuario(SesionUsuario.idActual)
                .collect { viajes ->
                    _estado.update { it.copy(totalViajesPublicados = viajes.size) }
                }
        }
    }

    fun alCambiarFoto(uri: String) {
        viewModelScope.launch {
            val usuarioActual = _estado.value.usuario ?: return@launch
            val actualizado = usuarioActual.copy(urlFoto = uri)
            repositorioUsuario.guardarUsuario(actualizado.aEntidad())
        }
    }

    fun alGuardarBiografia(nuevaBio: String) {
        viewModelScope.launch {
            val usuarioActual = _estado.value.usuario ?: return@launch
            val actualizado = usuarioActual.copy(biografia = nuevaBio)
            repositorioUsuario.guardarUsuario(actualizado.aEntidad())
        }
    }

    fun alPulsarEditarBiografia() = _estado.update { it.copy(mostrarDialogoBiografia = true) }
    fun alCerrarDialogoBiografia() = _estado.update { it.copy(mostrarDialogoBiografia = false) }

    fun alPulsarVerificarDni() {
        _estado.update { it.copy(mostrarDialogoVerificacion = true) }
    }

    fun alCerrarDialogoVerificacion() {
        _estado.update { it.copy(mostrarDialogoVerificacion = false) }
    }

    fun alConfirmarVerificacion() {
        // En producción aquí llamaríamos a un servicio externo (Stripe Identity, Jumio...)
        // De momento lo simulamos guardando dniVerificado = true en Room
        viewModelScope.launch {
            val usuarioActual = _estado.value.usuario ?: return@launch
            val actualizado = usuarioActual.copy(dniVerificado = true)
            repositorioUsuario.guardarUsuario(actualizado.aEntidad())
            _estado.update { it.copy(mostrarDialogoVerificacion = false) }
        }
    }

    fun cerrarSesion(contexto: Context, alTerminar: () -> Unit) {
        WebAuthProvider.logout(cuentaAuth0)
            .withScheme("demo")
            .start(contexto, object : Callback<Void?, AuthenticationException> {
                override fun onSuccess(result: Void?) {
                    viewModelScope.launch {
                        _estado.update { EstadoUiPerfil(sesionCerrada = true) }
                        alTerminar()
                    }
                }
                override fun onFailure(error: AuthenticationException) {
                    viewModelScope.launch {
                        _estado.update { it.copy(error = error.message) }
                    }
                }
            })
    }
}