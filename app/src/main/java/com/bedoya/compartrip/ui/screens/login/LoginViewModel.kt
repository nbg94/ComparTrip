package com.bedoya.compartrip.ui.screens.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.bedoya.compartrip.ConfiguracionAuth0
import com.bedoya.compartrip.SesionUsuario
import com.bedoya.compartrip.data.local.entity.EntidadUsuario
import com.bedoya.compartrip.data.repository.RepositorioUsuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EstadoUiLogin(
    val estaCargando: Boolean = false,
    val error: String? = null,
    val loginExitoso: Boolean = false,
    val idUsuario: String? = null,
    val nombreUsuario: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repositorioUsuario: RepositorioUsuario
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoUiLogin())
    val estado: StateFlow<EstadoUiLogin> = _estado.asStateFlow()

    private val cuentaAuth0 = Auth0(
        ConfiguracionAuth0.CLIENT_ID,
        ConfiguracionAuth0.DOMINIO
    )

    fun iniciarSesion(contexto: Context) {
        _estado.update { it.copy(estaCargando = true, error = null) }

        WebAuthProvider
            .login(cuentaAuth0)
            .withScheme("demo")
            .withScope("openid profile email")
            .start(contexto, object : Callback<Credentials, AuthenticationException> {

                override fun onSuccess(result: Credentials) {
                    viewModelScope.launch {
                        val perfil = result.user

                        // Guardamos el usuario en Room con los datos de Auth0
                        // Si ya existe lo actualiza (onConflict = REPLACE en el DAO)
                        repositorioUsuario.guardarUsuario(
                            EntidadUsuario(
                                idUsuario = perfil.getId() ?: perfil.email ?: "sin_id",
                                nombre = perfil.name ?: "Usuario",
                                correo = perfil.email ?: "",
                                urlFoto = perfil.pictureURL?.toString()
                            )
                        )

                        SesionUsuario.idActual = perfil.getId() ?: perfil.email ?: "sin_id"
                        SesionUsuario.nombreActual = perfil.name ?: "Usuario"
                        SesionUsuario.urlFotoActual = perfil.pictureURL?.toString()

                        _estado.update {
                            it.copy(
                                estaCargando = false,
                                loginExitoso = true,
                                idUsuario = perfil.getId() ?: perfil.email,
                                nombreUsuario = perfil.name
                            )
                        }
                    }
                }

                override fun onFailure(error: AuthenticationException) {
                    viewModelScope.launch {
                        _estado.update {
                            it.copy(
                                estaCargando = false,
                                error = if (error.isCanceled) null else error.message
                            )
                        }
                    }
                }
            })
    }

    fun cerrarSesion(contexto: Context, alTerminar: () -> Unit) {
        WebAuthProvider
            .logout(cuentaAuth0)
            .withScheme("demo")
            .start(contexto, object : Callback<Void?, AuthenticationException> {
                override fun onSuccess(result: Void?) {
                    viewModelScope.launch {
                        _estado.update { EstadoUiLogin() }
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